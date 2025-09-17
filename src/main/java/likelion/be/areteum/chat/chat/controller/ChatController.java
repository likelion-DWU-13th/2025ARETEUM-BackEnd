package likelion.be.areteum.chat.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    // '/app/chat'
    @MessageMapping("/chat") //send destination
    @SendTo("/topic/public") //subscribe destination
    //DTO 생기면 변경
    public String chat(String message){
        System.out.println("받은 메시지: " + message);
        return message;
    }
}
