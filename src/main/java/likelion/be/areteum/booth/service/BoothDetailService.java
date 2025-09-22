package likelion.be.areteum.booth.service;

import likelion.be.areteum.booth.dto.BoothDetailRes;
import likelion.be.areteum.booth.entity.Booth;
import likelion.be.areteum.booth.entity.BoothSchedule;
import likelion.be.areteum.booth.repository.BoothRepository;
import likelion.be.areteum.booth.repository.BoothScheduleRepository;
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

    public BoothDetailRes getDetail(Integer boothId) {
        Booth b = boothRepo.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("부스를 찾을 수 없습니다: " + boothId));

        // 전체 타임라인
        List<BoothSchedule> all = schRepo.findByBoothIdOrderByEventDateAscStartTimeAsc(boothId);
        var schedules = all.stream()
                .map(s -> new BoothDetailRes.ScheduleItem(s.getEventDate(), s.getStartTime(), s.getEndTime()))
                .toList();

        return new BoothDetailRes(
                b.getId(), b.getName(), b.getCategory(), b.getSubCategory(),
                b.getOrganizer(), b.getDescription(), b.getLocation(), b.getMapImageUrl(),
                null, List.of(), schedules // focusDate=null, focusTimes=빈 리스트
        );
    }
}


//public class BoothDetailService {
//
//    private final BoothRepository boothRepo;
//    private final BoothScheduleRepository schRepo;
//
//    public BoothDetailRes getDetail(Integer boothId, LocalDate date) {
//        Booth b = boothRepo.findById(boothId)
//                .orElseThrow(() -> new IllegalArgumentException("부스를 찾을 수 없습니다: " + boothId));
//
//        // 전체 타임라인
//        List<BoothSchedule> all = schRepo.findByBoothIdOrderByEventDateAscStartTimeAsc(boothId);
//        var schedules = all.stream()
//                .map(s -> new BoothDetailRes.ScheduleItem(s.getEventDate(), s.getStartTime(), s.getEndTime()))
//                .toList();
//
//        // focus 날짜
//        LocalDate focusDate = null;
//        List<BoothDetailRes.TimeRange> focusTimes = List.of();
//        if (date != null) {
//            focusDate = date;
//            var slots = schRepo.findByBoothIdAndEventDateOrderByStartTimeAsc(boothId, date);
//            focusTimes = slots.stream()
//                    .map(s -> new BoothDetailRes.TimeRange(s.getStartTime(), s.getEndTime()))
//                    .toList();
//        }
//
//        return new BoothDetailRes(
//                b.getId(), b.getName(), b.getCategory(), b.getSubCategory(),
//                b.getOrganizer(), b.getDescription(), b.getLocation(), b.getMapImageUrl(),
//                focusDate, focusTimes, schedules
//        );
//    }
//}
