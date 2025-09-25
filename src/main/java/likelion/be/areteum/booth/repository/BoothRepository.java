package likelion.be.areteum.booth.repository;

import likelion.be.areteum.booth.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoothRepository extends JpaRepository<Booth, Integer> {
    List<Booth> findByCategory(Category category);
    List<Booth> findByCategoryAndSubCategory(Category c, SubCategory s);
    List<Booth> findByNameContainingIgnoreCase(String name);
    Optional<Booth> findByNameIgnoreCase(String name); // 업서트용
}
