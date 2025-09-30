package likelion.be.areteum.booth.service;

import likelion.be.areteum.booth.entity.*;
import likelion.be.areteum.booth.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepo;
    private final BoothRepository boothRepo;

    public List<Menu> getMenus(Integer boothId) {
        return menuRepo.findByBoothId(boothId);
    }

    public Menu addMenu(Integer boothId, String name, MenuCategory category, Integer price, String note) {
        Booth booth = boothRepo.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("부스 없음: " + boothId));
        Menu menu = Menu.builder()
                .booth(booth)
                .name(name)
                .category(category)
                .price(price)
                .note(note)
                .build();
        return menuRepo.save(menu);
    }
}