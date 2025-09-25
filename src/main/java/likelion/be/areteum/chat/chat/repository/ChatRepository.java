package likelion.be.areteum.chat.chat.repository;

import likelion.be.areteum.chat.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    List<ChatEntity> findTop100ByOrderByCreatedAtAscIdAsc();
}
