package likelion.be.areteum.booth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion.be.areteum.booth.dto.BoothSearchRes;
import likelion.be.areteum.booth.entity.Category;
import likelion.be.areteum.booth.service.BoothCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name="Booth Card API", description = "부스 목록 검색 API - 담당(고소영)")
@RestController
@RequestMapping("/api/booth-cards")
@RequiredArgsConstructor
public class BoothCardController {

    private final BoothCardService cardService;

    @Operation(summary = "부스 목록 검색", description = """
        - date 파라미터를 보내지 않으면 자동으로 날짜가 설정됨<br>
        - 오늘이 축제 기간(9/30~10/2)일 경우 기본값: 오늘 날짜<br>
        - 오늘이 축제 기간이 아닐 경우 기본값: 축제 첫날(9/30)
        """)
    @GetMapping
    public BoothSearchRes list(
            @Parameter(description = "날짜 버튼 클릭 시 사용", example = "2025-09-30")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,

            @Parameter(description = "부스 카테고리(카테고리별 필터링 시 사용)")
            @RequestParam(required = false) Category category,

            @Parameter(description = "검색 키워드")
            @RequestParam(required = false, name="q") String query
    ) {
        if (date == null) {
            LocalDate today = LocalDate.now();
            LocalDate festivalStart = LocalDate.of(2025, 9, 30);
            LocalDate festivalEnd = LocalDate.of(2025, 10, 2);

            if (!today.isBefore(festivalStart) && !today.isAfter(festivalEnd)) {
                date = today;
            } else {
                date = festivalStart;
            }
        }
        return cardService.findCards(date, category, query);
    }
}