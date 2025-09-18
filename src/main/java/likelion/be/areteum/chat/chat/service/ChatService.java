package likelion.be.areteum.chat.chat.service;

import likelion.be.areteum.chat.chat.dto.ChatDto;
import likelion.be.areteum.chat.chat.entity.ChatEntity;
import likelion.be.areteum.chat.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd, hh:mm a");

    //채팅 저장
    public void saveMessage(String content){
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setContent(content);
        chatEntity.setCreatedAt(LocalDateTime.now());
        chatRepository.save(chatEntity);
    }

    //최근 100개 불러오기
    public List<ChatDto> getRecentMessages(){
        List<ChatEntity> messages = chatRepository.findTop100ByOrderByCreatedAtDesc();

        //엔티티를 dto로 변환
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 엔티티를 dto로 변환
    private ChatDto convertToDto(ChatEntity chatEntity){
        ChatDto dto = new ChatDto();
        dto.setId(chatEntity.getId());
        dto.setContent(chatEntity.getContent());
        dto.setCreatedAt(chatEntity.getCreatedAt());
        return dto;
    }
}
