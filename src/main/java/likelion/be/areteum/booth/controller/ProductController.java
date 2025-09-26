package likelion.be.areteum.booth.controller;

import likelion.be.areteum.booth.entity.Product;
import likelion.be.areteum.booth.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/api/booths/{boothId}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getProducts(@PathVariable Integer boothId) {
        return productService.getProducts(boothId);
    }

    @PostMapping
    public Product addProduct(@PathVariable Integer boothId,
                              @RequestParam String name) {
        return productService.addProduct(boothId, name);
    }
}