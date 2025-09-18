package likelion.be.areteum.booth.controller;

import likelion.be.areteum.booth.dto.BoothCardRes;
import likelion.be.areteum.booth.entity.Category;
import likelion.be.areteum.booth.entity.SubCategory;
import likelion.be.areteum.booth.service.BoothCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/booth-cards")
@RequiredArgsConstructor
public class BoothCardController {

    private final BoothCardService cardService;

    @GetMapping
    public Page<BoothCardRes> list(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) SubCategory subCategory,
            @RequestParam(required = false, name="q") String query,
            Pageable pageable
    ) {
        return cardService.findCards(date, category, subCategory, query, pageable);
    }
}
