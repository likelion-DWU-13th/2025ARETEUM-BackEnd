package likelion.be.areteum.chat.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // '/ws'로 연결하는 endpoint 생성, CORS 허용
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        //메시지 브로커가 처리, 서버 -> 클라이언트
        registry.enableSimpleBroker("/topic");
        //메시지 핸들러로 라우팅, 클라이언트 -> 서버
        registry.setApplicationDestinationPrefixes("/app");
    }
}
