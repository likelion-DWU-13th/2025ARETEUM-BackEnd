package likelion.be.areteum.chat.chat.service;

import likelion.be.areteum.chat.chat.dto.ChatDto;
import likelion.be.areteum.chat.chat.entity.ChatEntity;
import likelion.be.areteum.chat.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRepository chatRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd, hh:mm a");

    //채팅 저장
    public void saveMessageAndSend(String clientId, String content) {
        // 1. DB에 메시지 저장
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setClientId(clientId);
        chatEntity.setContent(content);
        chatEntity.setCreatedAt(LocalDateTime.now());
        chatRepository.save(chatEntity);

        ChatDto chatDto = convertToDto(chatEntity);
        messagingTemplate.convertAndSend("/topic/public", chatDto);
    }

  //최근 100개 불러오기
    public List<ChatDto> getRecentMessages() {
        List<ChatEntity> messages = chatRepository.findTop100ByOrderByCreatedAtDescIdDesc();
        return messages.stream()
                .sorted(Comparator.comparing(ChatEntity::getCreatedAt)
                        .thenComparing(ChatEntity::getId)) // 생성 시간 및 ID 오름차순(ASC)으로 정렬
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 엔티티를 dto로 변환
    private ChatDto convertToDto(ChatEntity chatEntity){
        ChatDto dto = new ChatDto();
        dto.setId(chatEntity.getId());
        dto.setClientId(chatEntity.getClientId());
        dto.setContent(chatEntity.getContent());
        dto.setCreatedAt(chatEntity.getCreatedAt());
        return dto;
    }
}
