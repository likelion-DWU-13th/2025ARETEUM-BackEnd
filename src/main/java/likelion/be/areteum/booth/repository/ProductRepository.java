package likelion.be.areteum.booth.repository;

import likelion.be.areteum.booth.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByBoothId(Integer boothId);
}