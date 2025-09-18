package likelion.be.areteum.booth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import likelion.be.areteum.booth.entity.Category;
import likelion.be.areteum.booth.entity.SubCategory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record BoothDetailRes(
        Integer id,
        String name,
        Category category,
        SubCategory subCategory,
        String organizer,
        String description,
        String location,
        String mapImageUrl,
        LocalDate focusDate,
        List<TimeRange> focusDateTimes,
        List<ScheduleItem> schedules
) {
    public record TimeRange(
            @JsonFormat(pattern="HH:mm") LocalTime start,
            @JsonFormat(pattern="HH:mm") LocalTime end
    ) {}

    public record ScheduleItem(
            LocalDate date,
            @JsonFormat(pattern="HH:mm") LocalTime start,
            @JsonFormat(pattern="HH:mm") LocalTime end
    ) {}
}
