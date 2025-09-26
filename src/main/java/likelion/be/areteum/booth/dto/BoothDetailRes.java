package likelion.be.areteum.booth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import likelion.be.areteum.booth.entity.Category;
import likelion.be.areteum.booth.entity.SubCategory;

import java.awt.*;
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
        String detailLocation,
        String mapImageUrl,
        String timeNote,
        LocalDate focusDate,
        List<TimeRange> focusDateTimes,
        List<ScheduleItem> schedules,
        List<MenuItem> menus,       // 주점일 경우
        List<SetMenuItem> setMenus,
        List<ExtraItem> extras,

        List<ProductItem> products  // 마켓일 경우
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

    public record MenuItem(
            String name,
            String category, // "ANJU" or "DRINK"
            Integer price,
            String note
    ) {}

    public record ProductItem(
            String name
    ) {}

    public record SetMenuItem(
            String name,
            Integer price,
            String benefit,
            List<String> items
    ) {}

    public record ExtraItem(
            String content
    ) {}
}
