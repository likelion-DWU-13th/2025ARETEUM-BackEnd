package likelion.be.areteum.booth.repository;

import likelion.be.areteum.booth.entity.SetMenuVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetMenuVariantRepository extends JpaRepository<SetMenuVariant, Integer> {
    List<SetMenuVariant> findBySetMenuId(Integer setMenuId);
}
