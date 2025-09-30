package likelion.be.areteum.booth.service;

import likelion.be.areteum.booth.dto.BoothCardRes;
import likelion.be.areteum.booth.dto.BoothSearchRes;
import likelion.be.areteum.booth.entity.BoothSchedule;
import likelion.be.areteum.booth.entity.Category;
import likelion.be.areteum.booth.repository.BoothScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoothCardService {

    private final BoothScheduleRepository scheduleRepo;

    public BoothSearchRes findCards(LocalDate date,
                                    Category category,
                                    String query) {
        String q = (StringUtils.hasText(query) ? query : null);

        List<BoothSchedule> schedules = scheduleRepo.filterAll(date, category, q);

        List<BoothCardRes> boothCards = schedules.stream()
                .map(s -> new BoothCardRes(
                        s.getBooth().getId(),
                        s.getBooth().getName(),
                        s.getBooth().getCategory(),
                        s.getBooth().getSubCategory(),
                        s.getBooth().getLocation(),
                        s.getEventDate(),
                        s.getStartTime(),
                        s.getEndTime()
                ))
                .toList();

        return new BoothSearchRes(boothCards, boothCards.size());
    }
}