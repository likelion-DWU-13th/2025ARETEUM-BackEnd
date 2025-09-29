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
    public void saveMessage(String clientId, String content){
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setClientId(clientId);
        chatEntity.setContent(content);
        chatEntity.setCreatedAt(LocalDateTime.now());
        chatRepository.save(chatEntity);
    }

  //최근 100개 불러오기
    public List<ChatDto> getRecentMessages() {
        // 1. DB에서 가장 최근 메시지 100개를 가져옵니다 (id와 생성 시간 내림차순).
        // 이렇게 해야 DB에 저장된 마지막 100개의 메시지가 선택됩니다.
        List<ChatEntity> messages = chatRepository.findTop100ByOrderByCreatedAtDescIdDesc();

        // 2. 가져온 메시지 리스트(최신순)를 다시 시간 오름차순으로 정렬하여 반환합니다.
        // 이렇게 해야 채팅 화면에서 시간 순서대로 (과거 -> 최신) 표시됩니다.
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
