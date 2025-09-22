package likelion.be.areteum.booth.controller;

import likelion.be.areteum.booth.dto.BoothDetailRes;
import likelion.be.areteum.booth.service.BoothDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/booths")
@RequiredArgsConstructor

public class BoothDetailController {

    private final BoothDetailService detailService;

    @GetMapping("/{id}")
    public BoothDetailRes getDetail(@PathVariable Integer id) {
        return detailService.getDetail(id);
    }
}


//public class BoothDetailController {
//
//    private final BoothDetailService detailService;
//
//    @GetMapping("/{id}")
//    public BoothDetailRes getDetail(
//            @PathVariable Integer id,
//            @RequestParam(required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
//    ) {
//        return detailService.getDetail(id, date);
//    }
//}
