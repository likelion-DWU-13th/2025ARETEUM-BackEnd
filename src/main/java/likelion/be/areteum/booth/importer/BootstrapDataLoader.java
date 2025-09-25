package likelion.be.areteum.booth.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.be.areteum.booth.dto.*;
import likelion.be.areteum.booth.entity.*;
import likelion.be.areteum.booth.repository.BoothRepository;
import likelion.be.areteum.booth.repository.BoothScheduleRepository;
import likelion.be.areteum.booth.service.BoothScheduleService;
import likelion.be.areteum.booth.service.BoothService;
import lombok.RequiredArgsConstructor;
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

@Component
@Profile("bootstrap")
@RequiredArgsConstructor
public class BootstrapDataLoader implements CommandLineRunner {

    private final BoothService boothSvc;
    private final BoothScheduleService schSvc;
    private final BoothRepository boothRepo;               // 삭제용
    private final BoothScheduleRepository schRepo;         // 삭제용
    private final ObjectMapper om;

    @Value("${seed.mergeUpsert:true}") boolean mergeUpsert;
    @Value("${seed.deleteMissingBooths:true}") boolean deleteMissingBooths;
    @Value("${seed.scheduleStrategy:DIFF}") String scheduleStrategy; // REPLACE|MERGE|DIFF
    @Value("${seed.clearSchedulesIfMissingInSeed:false}") boolean clearSchedulesIfMissingInSeed;

    // JSON 매핑용
    record BoothImport(String name, Category category, SubCategory subCategory,
                       String description, String location, String organizer, String mapImageUrl) {}
    record SchedImport(String boothName, LocalDate eventDate, LocalTime startTime, LocalTime endTime) {}

    @Override @Transactional
    public void run(String... args) throws Exception {
        Map<String, Integer> nameToId = new HashMap<>();   // booth name → id
        Set<String> targetNames = new HashSet<>();         // 시드 부스들(삭제 판정)

        // 1) 부스 업서트 + 타깃 수집
        try (InputStream in = new ClassPathResource("booths.json").getInputStream()) {
            List<BoothImport> booths = om.readValue(in, new TypeReference<List<BoothImport>>() {});
            for (BoothImport b : booths) {
                targetNames.add(b.name().toLowerCase());
                BoothCreateReq req = new BoothCreateReq(
                        b.name(),
                        b.category(),
                        b.subCategory() == null ? SubCategory.NONE : b.subCategory(),
                        b.description(),
                        b.location(),
                        b.organizer(),
                        b.mapImageUrl()
                );
                BoothDetailRes res = mergeUpsert ? boothSvc.upsertByName(req) : boothSvc.create(req);
                nameToId.put(res.name().toLowerCase(), res.id());
            }
        }

        // 2) 부스 삭제(옵션) — 스케줄 먼저 삭제
        if (deleteMissingBooths) {
            var all = boothRepo.findAll();
            for (var bo : all) {
                String key = bo.getName().toLowerCase();
                if (!targetNames.contains(key)) {
                    schRepo.deleteByBoothId(bo.getId());
                    boothRepo.delete(bo);
                }
            }
        }

        // 3) 스케줄 시드 로드 → 그룹핑
        Map<Integer, List<BoothScheduleCreateReq>> grouped = new HashMap<>();
        Set<Integer> boothsMentionedInScheduleSeed = new HashSet<>();
        try (InputStream in = new ClassPathResource("schedules.json").getInputStream()) {
            List<SchedImport> slots = om.readValue(in, new TypeReference<List<SchedImport>>() {});
            for (SchedImport s : slots) {
                Integer boothId = nameToId.get(s.boothName().toLowerCase());
                if (boothId == null) throw new IllegalArgumentException("부스 매칭 실패: " + s.boothName());
                boothsMentionedInScheduleSeed.add(boothId);
                grouped.computeIfAbsent(boothId, k -> new ArrayList<>())
                        .add(new BoothScheduleCreateReq(s.eventDate(), s.startTime(), s.endTime()));
            }
        }

        // 4) 전략 분기
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

        // 5) schedules.json에 언급조차 안 된 부스 — 선택적 정리
        if (clearSchedulesIfMissingInSeed) {
            for (Integer boothId : nameToId.values()) {
                if (!boothsMentionedInScheduleSeed.contains(boothId)) {
                    schSvc.replaceSchedules(boothId, List.of());
                }
            }
        }
    }
}
