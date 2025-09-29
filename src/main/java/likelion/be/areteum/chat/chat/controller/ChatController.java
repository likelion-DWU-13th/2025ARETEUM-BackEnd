package likelion.be.areteum.chat.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion.be.areteum.chat.chat.dto.ChatDto;
import likelion.be.areteum.chat.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="Chat API", description = "채팅 API - 담당(이주연, 최이주)")
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat") //send destination
    public void sendMessage(ChatDto message){
        chatService.saveMessageAndSend(message.getClientId(), message.getContent());
    }

    // 최근 100개 불러오기 (REST API 호출)
    @GetMapping("/chat/history")
    @Operation(summary = "최근 100개 채팅 불러오기")
    public List<ChatDto> getRecentMessage(){
        return chatService.getRecentMessages();
    }
}