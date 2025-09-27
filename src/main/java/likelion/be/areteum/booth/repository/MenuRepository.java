package likelion.be.areteum.booth.repository;

import likelion.be.areteum.booth.entity.Menu;
import likelion.be.areteum.booth.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByBoothId(Integer boothId);
    Optional<Menu> findByBoothIdAndNameAndCategory(Integer boothId, String name, MenuCategory category);

}
