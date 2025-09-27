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
public class ProductService {

    private final ProductRepository productRepo;
    private final BoothRepository boothRepo;

    public List<Product> getProducts(Integer boothId) {
        return productRepo.findByBoothId(boothId);
    }

    public Product addProduct(Integer boothId, String name) {
        Booth booth = boothRepo.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("부스 없음: " + boothId));
        Product product = Product.builder()
                .booth(booth)
                .name(name)
                .build();
        return productRepo.save(product);
    }
}