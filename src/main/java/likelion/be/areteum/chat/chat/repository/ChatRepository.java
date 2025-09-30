package likelion.be.areteum.chat.chat.repository;

import likelion.be.areteum.chat.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    @Query(value = "select * from chat_entity order by id desc limit 100", nativeQuery = true)
    List<ChatEntity> findRecent100();
}