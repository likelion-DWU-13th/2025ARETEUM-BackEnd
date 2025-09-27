package likelion.be.areteum.booth.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.be.areteum.booth.dto.*;
import likelion.be.areteum.booth.entity.*;
import likelion.be.areteum.booth.repository.BoothRepository;
import likelion.be.areteum.booth.repository.BoothScheduleRepository;
import likelion.be.areteum.booth.repository.MenuRepository;
import likelion.be.areteum.booth.repository.ProductRepository;
import likelion.be.areteum.booth.service.BoothScheduleService;
import likelion.be.areteum.booth.service.BoothService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Component
@Profile("bootstrap")
@RequiredArgsConstructor
public class BootstrapDataLoader implements CommandLineRunner {

    private final BoothService boothSvc;
    private final BoothScheduleService schSvc;
    private final BoothRepository boothRepo;
    private final BoothScheduleRepository schRepo;
    private final ProductRepository productRepo;
    private final MenuRepository menuRepo;
    private final ObjectMapper om;

    @Value("${seed.mergeUpsert:true}") boolean mergeUpsert;
    @Value("${seed.deleteMissingBooths:true}") boolean deleteMissingBooths;
    @Value("${seed.scheduleStrategy:DIFF}") String scheduleStrategy; // REPLACE|MERGE|DIFF
    @Value("${seed.clearSchedulesIfMissingInSeed:false}") boolean clearSchedulesIfMissingInSeed;

    // ===== JSON 매핑용 DTO(record) =====
    record BoothImport(
            String name,
            Category category,
            SubCategory subCategory,
            String description,
            String location,
            String detailLocation,
            String organizer,
            String mapImageUrl,
            String timeNote,
            List<ProductImport> products,
            List<MenuImport> menus
    ) {}
    record SchedImport(String boothName, LocalDate eventDate, LocalTime startTime, LocalTime endTime) {}
    record ProductImport(String name) {}
    // note 는 선택. 없으면 null 로 들어옴.
    record MenuImport(String name, String category, Integer price, String note) {}

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Map<String, Integer> nameToId = new HashMap<>(); // booth name → id
        Set<String> targetNames = new HashSet<>();       // 시드 부스(삭제 판정용)

        // 1) 부스 업서트 + 이름→ID 매핑 수집
        try (InputStream in = new ClassPathResource("booths.json").getInputStream()) {
            List<BoothImport> booths = om.readValue(in, new TypeReference<>() {});
            for (BoothImport b : booths) {
                if (b.name() == null || b.category() == null) {
                    log.warn("부스 스킵(필수 값 누락): name={}, category={}", b.name(), b.category());
                    continue;
                }

                targetNames.add(b.name().toLowerCase());

                BoothCreateReq req = new BoothCreateReq(
                        b.name(),
                        b.category(),
                        b.subCategory() == null ? SubCategory.NONE : b.subCategory(),
                        b.description(),
                        b.location(),
                        b.detailLocation(),
                        b.organizer(),
                        b.mapImageUrl(),
                        b.timeNote() == null ? "" : b.timeNote()
                );

                BoothDetailRes res = mergeUpsert ? boothSvc.upsertByName(req) : boothSvc.create(req);
                nameToId.put(res.name().toLowerCase(), res.id());

                Booth boothEntity = boothRepo.findById(res.id()).orElseThrow();

                // ----- 상품 업서트 -----
                if (b.products() != null) {
                    for (ProductImport p : b.products()) {
                        if (p == null || p.name() == null || p.name().isBlank()) {
                            log.warn("Product 스킵(이름 누락): booth={}", boothEntity.getName());
                            continue;
                        }
                        productRepo.findByBoothIdAndName(boothEntity.getId(), p.name())
                                .ifPresentOrElse(
                                        exist -> { /* 필요 시 필드 업데이트 추가 */ },
                                        () -> productRepo.save(Product.builder()
                                                .booth(boothEntity)
                                                .name(p.name())
                                                .build())
                                );
                    }
                }

                // ----- 메뉴 업서트 -----
                if (b.menus() != null) {
                    for (MenuImport m : b.menus()) {
                        if (m == null) continue;

                        // 1) 필수값 체크
                        if (m.name() == null || m.name().isBlank()) {
                            log.warn("Menu 스킵(name 누락): booth={}", boothEntity.getName());
                            continue;
                        }
                        if (m.category() == null || m.category().isBlank()) {
                            // JSON에 setMenus 같은 다른 구조가 섞여 들어온 경우 여기로 옵니다.
                            log.warn("Menu 스킵(category 누락): booth={}, name={}", boothEntity.getName(), m.name());
                            continue;
                        }

                        // 2) 카테고리 파싱 (오타/미지원 값 방지)
                        final MenuCategory cat;
                        try {
                            cat = MenuCategory.from(m.category());
                        } catch (IllegalArgumentException ex) {
                            log.warn("Menu 스킵(카테고리 파싱 실패): booth={}, name={}, raw={}", boothEntity.getName(), m.name(), m.category());
                            continue;
                        }

                        // 3) 업서트 (있으면 갱신, 없으면 생성)
                        menuRepo.findByBoothIdAndNameAndCategory(boothEntity.getId(), m.name(), cat)
                                .ifPresentOrElse(existing -> {
                                            // 갱신
                                            existing.setPrice(m.price());
                                            // note 필드가 엔티티에 있다면 같이 반영
                                            try { existing.getClass().getMethod("setNote", String.class); existing.setNote(m.note()); } catch (NoSuchMethodException ignore) {}
                                        },
                                        () -> {
                                            Menu.MenuBuilder builder = Menu.builder()
                                                    .booth(boothEntity)
                                                    .name(m.name())
                                                    .category(cat)
                                                    .price(m.price());
                                            // note 필드가 있으면 세팅
                                            try { builder.getClass().getMethod("note", String.class); builder.note(m.note()); } catch (NoSuchMethodException ignore) {}
                                            menuRepo.save(builder.build());
                                        }
                                );
                    }
                }
            }
        }

        // 2) 시드에 없는 부스 삭제(옵션) — 스케줄 먼저
        if (deleteMissingBooths) {
            for (var bo : boothRepo.findAll()) {
                String key = bo.getName().toLowerCase();
                if (!targetNames.contains(key)) {
                    schRepo.deleteByBoothId(bo.getId());
                    boothRepo.delete(bo);
                }
            }
        }

        // 3) 스케줄 시드 로드
        Map<Integer, List<BoothScheduleCreateReq>> grouped = new HashMap<>();
        Set<Integer> boothsMentionedInScheduleSeed = new HashSet<>();
        try (InputStream in = new ClassPathResource("schedules.json").getInputStream()) {
            List<SchedImport> slots = om.readValue(in, new TypeReference<>() {});
            for (SchedImport s : slots) {
                if (s.boothName() == null) {
                    log.warn("스케줄 스킵(boothName 누락): {}", s);
                    continue;
                }
                Integer boothId = nameToId.get(s.boothName().toLowerCase());
                if (boothId == null) {
                    throw new IllegalArgumentException("부스 매칭 실패: " + s.boothName());
                }
                boothsMentionedInScheduleSeed.add(boothId);
                grouped.computeIfAbsent(boothId, k -> new ArrayList<>())
                        .add(new BoothScheduleCreateReq(s.eventDate(), s.startTime(), s.endTime()));
            }
        }

        // 4) 스케줄 전략별 반영
        String strategy = (scheduleStrategy == null ? "DIFF" : scheduleStrategy.trim().toUpperCase());
        for (var e : grouped.entrySet()) {
            Integer boothId = e.getKey();
            List<BoothScheduleCreateReq> reqs = e.getValue();
            switch (strategy) {
                case "REPLACE" -> schSvc.replaceSchedules(boothId, reqs);
                case "MERGE"   -> schSvc.addSchedules(boothId, reqs);
                case "DIFF"    -> schSvc.syncSchedulesDiff(boothId, reqs);
                default -> throw new IllegalArgumentException("지원하지 않는 scheduleStrategy: " + scheduleStrategy);
            }
        }

        // 5) schedules.json 에 언급 안 된 부스 스케줄 정리(옵션)
        if (clearSchedulesIfMissingInSeed) {
            for (Integer boothId : nameToId.values()) {
                if (!boothsMentionedInScheduleSeed.contains(boothId)) {
                    schSvc.replaceSchedules(boothId, List.of());
                }
            }
        }
    }
}
