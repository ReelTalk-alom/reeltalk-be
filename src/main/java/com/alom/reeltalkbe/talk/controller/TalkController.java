package com.alom.reeltalkbe.talk.controller;


import com.alom.reeltalkbe.talk.domain.TalkMessage;
import com.alom.reeltalkbe.talk.dto.TalkMessageDto;
import com.alom.reeltalkbe.talk.service.TalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TalkController {
    private final TalkService chatService;
    /**
     * 클라이언트가 /app/chat/{roomId}/sendMessage 로 메시지를 발행하면
     * => @MessageMapping("/chat/{roomId}/sendMessage") 메서드가 받음
     * => 처리 후 /topic/chat/room/{roomId} 로 메시지를 전송
     */
    @MessageMapping("/chat/{roomId}/sendMessage")
    @SendTo("/topic/chat/room/{roomId}")
    public TalkMessage sendMessage(@DestinationVariable TalkMessageDto chatMessageDto) {
        System.out.println("Message from" + chatMessageDto.getSender() + ", content" + chatMessageDto.getContent_id());
        // DB 저장 + 반환된 객체를 /topic/chat/room/{roomId} 로 구독중인 모든 세션에게 broadcast
        return chatService.saveMessage(chatMessageDto);
    }

}
