package likelion.be.areteum.booth.service;

import likelion.be.areteum.booth.dto.BoothCardRes;
import likelion.be.areteum.booth.entity.BoothSchedule;
import likelion.be.areteum.booth.entity.Category;
import likelion.be.areteum.booth.entity.SubCategory;
import likelion.be.areteum.booth.repository.BoothScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoothCardService {

    private final BoothScheduleRepository scheduleRepo;

    public Page<BoothCardRes> findCards(LocalDate date,
                                        Category category,
                                        SubCategory sub,
                                        String query,
                                        Pageable pageable) {
        String q = (StringUtils.hasText(query) ? query : null);

        Page<BoothSchedule> page = scheduleRepo.filterPaged(date, category, sub, q, pageable);

        return page.map(s -> new BoothCardRes(
                s.getBooth().getId(),
                s.getBooth().getName(),
                s.getBooth().getCategory(),
                s.getBooth().getSubCategory(),
                s.getBooth().getLocation(),
                s.getEventDate(),
                s.getStartTime(),
                s.getEndTime()
        ));
    }
}
