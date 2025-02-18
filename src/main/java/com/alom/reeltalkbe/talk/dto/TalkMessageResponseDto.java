package com.alom.reeltalkbe.talk.dto;

import com.alom.reeltalkbe.talk.domain.TalkMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TalkMessageResponseDto {
    private String message;
    private String sender;
    private Long contentId;
    private Long messageId;
    private Long userId;

    @Builder
    private TalkMessageResponseDto(String message, String sender, Long contentId, Long messageId, Long userId) {
        this.message = message;
        this.sender = sender;
        this.contentId = contentId;
        this.messageId = messageId;
        this.userId = userId;
    }

    public static TalkMessageResponseDto of(TalkMessage message) {
        return TalkMessageResponseDto
                .builder()
                .message(message.getMessage())
                .sender(message.getSender())
                .contentId(message.getContent().getId())
                .userId(message.getUser().getId())
                .messageId(message.getId())
                .build();
    }

    public static List<TalkMessageResponseDto> dtoListOf(List<TalkMessage> messageList) {
        List<TalkMessageResponseDto> list = new ArrayList<>();
        for(TalkMessage message : messageList) {
            list.add(
                    TalkMessageResponseDto
                            .builder()
                            .message(message.getMessage())
                            .sender(message.getSender())
                            .contentId(message.getContent().getId())
                            .userId(message.getUser().getId())
                            .messageId(message.getId())
                            .build()
            );
        }
        return list;
    }
}
