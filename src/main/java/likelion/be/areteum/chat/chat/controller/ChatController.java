package likelion.be.areteum.chat.chat.controller;

import likelion.be.areteum.chat.chat.dto.ChatDto;
import likelion.be.areteum.chat.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 실시간 메시지 수신 (클라이언트 → 서버)
    @MessageMapping("/chat")
    public void receiveMessage(ChatDto message) {
        chatService.saveMessageAndSend(message.getClientId(), message.getContent());
    }

    //100개
    @GetMapping("/chat/history")
    public List<ChatDto> getRecentMessages() {
       return chatService.getRecentMessages();
    }
}