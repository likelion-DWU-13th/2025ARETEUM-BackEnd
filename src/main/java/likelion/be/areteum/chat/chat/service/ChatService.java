package likelion.be.areteum.chat.chat.service;

import likelion.be.areteum.chat.chat.dto.ChatDto;
import likelion.be.areteum.chat.chat.entity.ChatEntity;
import likelion.be.areteum.chat.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRepository chatRepository;

    // 메시지 저장 + 실시간 전송
    public void saveMessageAndSend(String clientId, String content) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setClientId(clientId);
        chatEntity.setContent(content);
        chatEntity.setCreatedAt(LocalDateTime.now());

        chatRepository.save(chatEntity);

        ChatDto chatDto = convertToDto(chatEntity);
        messagingTemplate.convertAndSend("/topic/chat", chatDto);
    }

//    // 전체 메시지 가져오기 (초기 로딩)
//    public List<ChatDto> getAllMessages() {
//        List<ChatEntity> messages = chatRepository.findAllByOrderByCreatedAtAscIdAsc();
//        return messages.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }


    //100개 정렬
    public List<ChatDto> getRecentMessages(){
        List<ChatEntity> messages = chatRepository.findTop100ByOrderByIdDesc();

        Collections.reverse(messages);

        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ChatDto convertToDto(ChatEntity chatEntity) {
        ChatDto dto = new ChatDto();
        dto.setId(chatEntity.getId());
        dto.setClientId(chatEntity.getClientId());
        dto.setContent(chatEntity.getContent());
        dto.setCreatedAt(chatEntity.getCreatedAt());
        return dto;
    }
}
