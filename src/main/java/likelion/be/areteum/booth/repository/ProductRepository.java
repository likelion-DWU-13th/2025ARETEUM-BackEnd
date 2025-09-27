package likelion.be.areteum.booth.repository;

import likelion.be.areteum.booth.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByBoothId(Integer boothId);
    Optional<Product> findByBoothIdAndName(Integer boothId, String name);

}