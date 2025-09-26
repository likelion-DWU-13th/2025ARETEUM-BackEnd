package likelion.be.areteum.booth.controller;

import likelion.be.areteum.booth.entity.Menu;
import likelion.be.areteum.booth.entity.MenuCategory;
import likelion.be.areteum.booth.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Hidden;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/booths/{boothId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public List<Menu> getMenus(@PathVariable Integer boothId) {
        return menuService.getMenus(boothId);
    }

    @PostMapping
    public Menu addMenu(@PathVariable Integer boothId,
                        @RequestParam String name,
                        @RequestParam MenuCategory category,
                        @RequestParam Integer price,
                        @RequestParam(required = false) String note
    ) {
        return menuService.addMenu(boothId, name, category, price, note);
    }
}