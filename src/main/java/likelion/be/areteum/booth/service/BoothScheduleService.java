package likelion.be.areteum.booth.service;

import likelion.be.areteum.booth.dto.BoothDetailRes;
import likelion.be.areteum.booth.dto.BoothScheduleCreateReq;
import likelion.be.areteum.booth.entity.Booth;
import likelion.be.areteum.booth.entity.BoothSchedule;
import likelion.be.areteum.booth.repository.BoothRepository;
import likelion.be.areteum.booth.repository.BoothScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoothScheduleService {

    private final BoothRepository boothRepo;
    private final BoothScheduleRepository schRepo;

    // MERGE: 없는 것만 추가
    public List<BoothDetailRes.ScheduleItem> addSchedules(Integer boothId, List<BoothScheduleCreateReq> reqs) {
        Booth booth = boothRepo.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("부스 없음: " + boothId));
        validateReqs(reqs);

        List<BoothDetailRes.ScheduleItem> out = new ArrayList<>();
        for (BoothScheduleCreateReq r : reqs) {
            boolean dup = schRepo.existsByBoothIdAndEventDateAndStartTimeAndEndTime(
                    boothId, r.eventDate(), r.startTime(), r.endTime());
            if (dup) continue;

            BoothSchedule saved = schRepo.save(BoothSchedule.builder()
                    .booth(booth)
                    .eventDate(r.eventDate())
                    .startTime(r.startTime())
                    .endTime(r.endTime())
                    .build());
            out.add(new BoothDetailRes.ScheduleItem(saved.getEventDate(), saved.getStartTime(), saved.getEndTime()));
        }
        return out;
    }

    // REPLACE: 싹 지우고 다시 삽입
    public void replaceSchedules(Integer boothId, List<BoothScheduleCreateReq> reqs) {
        schRepo.deleteByBoothId(boothId);
        if (!reqs.isEmpty()) addSchedules(boothId, reqs);
    }

    // DIFF: 추가/삭제만 최소 변경
    public void syncSchedulesDiff(Integer boothId, List<BoothScheduleCreateReq> reqs) {
        Booth booth = boothRepo.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("부스 없음: " + boothId));
        validateReqs(reqs);

        record Key(LocalDate d, LocalTime s, LocalTime e) {}
        List<BoothSchedule> existing = schRepo.findByBoothId(boothId);

        Set<Key> current = existing.stream()
                .map(x -> new Key(x.getEventDate(), x.getStartTime(), x.getEndTime()))
                .collect(Collectors.toSet());

        Set<Key> target = reqs.stream()
                .map(r -> new Key(r.eventDate(), r.startTime(), r.endTime()))
                .collect(Collectors.toSet());

        // 삭제: 현재 - 타깃
        List<BoothSchedule> toDelete = existing.stream()
                .filter(x -> !target.contains(new Key(x.getEventDate(), x.getStartTime(), x.getEndTime())))
                .toList();
        if (!toDelete.isEmpty()) schRepo.deleteAllInBatch(toDelete);

        // 추가: 타깃 - 현재
        List<BoothSchedule> toInsert = reqs.stream()
                .filter(r -> !current.contains(new Key(r.eventDate(), r.startTime(), r.endTime())))
                .map(r -> BoothSchedule.builder()
                        .booth(booth)
                        .eventDate(r.eventDate())
                        .startTime(r.startTime())
                        .endTime(r.endTime())
                        .build())
                .toList();
        if (!toInsert.isEmpty()) schRepo.saveAll(toInsert);
    }

    private static void validateReqs(List<BoothScheduleCreateReq> reqs) {
        for (BoothScheduleCreateReq r : reqs) {
            if (!r.startTime().isBefore(r.endTime())) {
                throw new IllegalArgumentException("시작시간은 종료시간보다 빨라야 합니다: " + r);
            }
        }
    }
}
