package likelion.be.areteum.booth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import likelion.be.areteum.booth.entity.Category;
import likelion.be.areteum.booth.entity.SubCategory;

public record BoothCreateReq(
        @NotBlank String name,
        @NotNull  Category category,
        SubCategory subCategory,
        String description,
        String location,
        String organizer,
        String mapImageUrl
) {}
