package likelion.be.areteum.booth.repository;

import likelion.be.areteum.booth.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByBoothId(Integer boothId);
}
