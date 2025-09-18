package likelion.be.areteum.booth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import likelion.be.areteum.booth.entity.Category;
import likelion.be.areteum.booth.entity.SubCategory;
import java.time.LocalDate;
import java.time.LocalTime;

public record BoothCardRes(
        Integer boothId,
        String name,
        Category category,
        SubCategory subCategory,
        String location,
        LocalDate eventDate,
        @JsonFormat(pattern = "HH:mm") LocalTime startTime,
        @JsonFormat(pattern = "HH:mm") LocalTime endTime
) {}
