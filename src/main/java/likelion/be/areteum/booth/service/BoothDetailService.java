package likelion.be.areteum.booth.service;

import likelion.be.areteum.booth.dto.BoothDetailRes;
import likelion.be.areteum.booth.entity.Booth;
import likelion.be.areteum.booth.entity.BoothSchedule;
import likelion.be.areteum.booth.entity.Category;
import likelion.be.areteum.booth.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class BoothDetailService {

    private final BoothRepository boothRepo;
    private final BoothScheduleRepository schRepo;
    private final MenuRepository menuRepo;
    private final ProductRepository productRepo;
    private final SetMenuRepository setMenuRepo;

    public BoothDetailRes getDetail(Integer boothId) {
        Booth b = boothRepo.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("부스를 찾을 수 없습니다: " + boothId));

        // 전체 타임라인
        List<BoothSchedule> all = schRepo.findByBoothIdOrderByEventDateAscStartTimeAsc(boothId);
        var schedules = all.stream()
                .map(s -> new BoothDetailRes.ScheduleItem(s.getEventDate(), s.getStartTime(), s.getEndTime()))
                .toList();

        // 주점 메뉴
        List<BoothDetailRes.MenuItem> menus = List.of();
        if (b.getCategory() == Category.PUB) {
            menus = menuRepo.findByBoothId(boothId).stream()
                    .map(m -> new BoothDetailRes.MenuItem(m.getName(), m.getCategory().name(), m.getPrice(), m.getNote()))
                    .toList();
        }

        // 마켓 상품
        List<BoothDetailRes.ProductItem> products = List.of();
        if (b.getCategory() == Category.MARKET) {
            products = productRepo.findByBoothId(boothId).stream()
                    .map(p -> new BoothDetailRes.ProductItem(p.getName()))
                    .toList();
        }

        // 세트메뉴
        List<BoothDetailRes.SetMenuRes> setMenus = setMenuRepo.findByBoothId(boothId).stream()
                .map(s -> new BoothDetailRes.SetMenuRes(
                        s.getName(),
                        s.getVariants().stream()
                                .map(v -> new BoothDetailRes.VariantItem(v.getItems(), v.getPrice(), v.getNote()))
                                .toList()
                ))
                .toList();



        return new BoothDetailRes(
                b.getId(),
                b.getName(),
                b.getCategory(),
                b.getSubCategory(),
                b.getOrganizer(),
                b.getDescription(),
                b.getLocation(),
                b.getDetailLocation(),
                b.getMapImageUrl(),
                b.getTimeNote(),
                null, // focusDate: null 허용이면 이렇게 명시 캐스팅
                List.<BoothDetailRes.TimeRange>of(),
                schedules,
                menus,
                setMenus,
                products
        );
    }
}
