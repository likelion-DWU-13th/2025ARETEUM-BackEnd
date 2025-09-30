package likelion.be.areteum.booth.repository;

import likelion.be.areteum.booth.entity.SetMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SetMenuRepository extends JpaRepository<SetMenu, Integer> {
    List<SetMenu> findByBoothId(Integer boothId);
}