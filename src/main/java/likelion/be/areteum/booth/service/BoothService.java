package likelion.be.areteum.booth.service;

import likelion.be.areteum.booth.dto.BoothCreateReq;
import likelion.be.areteum.booth.dto.BoothDetailRes;
import likelion.be.areteum.booth.entity.Booth;
import likelion.be.areteum.booth.entity.SubCategory;
import likelion.be.areteum.booth.repository.BoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoothService {

    private final BoothRepository boothRepo;

    // 순수 생성
    public BoothDetailRes create(BoothCreateReq r) {
        Booth b = Booth.builder()
                .name(r.name())
                .category(r.category())
                .subCategory(r.subCategory() == null ? SubCategory.NONE : r.subCategory())
                .description(r.description())
                .location(r.location())
                .detailLocation(r.detailLocation())
                .organizer(r.organizer())
                .mapImageUrl(r.mapImageUrl())
                .timeNote(r.timeNote())
                .build();
        b = boothRepo.save(b);
        return toDetail(b);
    }

    // 이름(자연키) 기준 업서트: 있으면 수정, 없으면 생성
    public BoothDetailRes upsertByName(BoothCreateReq r) {
        Booth b = boothRepo.findByNameIgnoreCase(r.name()).orElseGet(Booth::new);
        b.setName(r.name());
        b.setCategory(r.category());
        b.setSubCategory(r.subCategory() == null ? SubCategory.NONE : r.subCategory());
        b.setDescription(r.description());
        b.setLocation(r.location());
        b.setDetailLocation(r.detailLocation());
        b.setOrganizer(r.organizer());
        b.setTimeNote(r.timeNote());
        b.setMapImageUrl(r.mapImageUrl());

        b = boothRepo.save(b);
        return toDetail(b);
    }

    private static BoothDetailRes toDetail(Booth b) {
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
                null,
                List.<BoothDetailRes.TimeRange>of(),
                List.<BoothDetailRes.ScheduleItem>of(),
                List.<BoothDetailRes.MenuItem>of(),
                List.<BoothDetailRes.SetMenuRes>of(),
                List.<BoothDetailRes.ProductItem>of()
        );
    }
}
