package likelion.be.areteum.booth.repository;

import likelion.be.areteum.booth.entity.Extra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExtraRepository extends JpaRepository<Extra, Integer> {
    List<Extra> findByBoothId(Integer boothId);
}
