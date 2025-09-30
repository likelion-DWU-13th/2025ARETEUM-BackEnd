package likelion.be.areteum.chat.chat.repository;

import likelion.be.areteum.chat.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    @Query(value = "SELECT * FROM (SELECT * FROM chat_entity ORDER BY id DESC LIMIT 100) AS recent_chats ORDER BY id ASC", nativeQuery = true)
    List<ChatEntity> findTop100ByIdOrderedAsc();
}