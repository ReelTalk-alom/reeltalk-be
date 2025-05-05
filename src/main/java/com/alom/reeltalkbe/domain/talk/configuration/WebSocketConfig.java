package com.alom.reeltalkbe.domain.talk.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 메시지 브로커 구성
     * - 클라이언트가 구독(subscribe)할 엔드포인트(prefix)
     * - 메시지 발행(publish)할 엔드포인트(prefix) 등을 설정
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        /*
        인메모리(Simple) 브로커 사용
        "/topic"은 브로드캐스트 용, "/queue"은 1:1 개인 메시지용 */
        config.enableSimpleBroker("/topic");

        // 클라이언트에서 메시지를 보낼 때 붙이는 prefix
        // ex) stompClient.send("/app/chat.sendMessage", {...})
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * 클라이언트가 websocket handshake를 할 때 연결할 endpoint
     * 여기서는 SockJS fallback도 지원
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOrigins("*")     // CORS 설정(실서비스에선 도메인 지정 권장)
                .withSockJS();              // SockJS 사용
    }
}
