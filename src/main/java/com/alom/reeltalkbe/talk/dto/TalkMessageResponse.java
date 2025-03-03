package com.alom.reeltalkbe.talk.dto;

import com.alom.reeltalkbe.talk.domain.TalkMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TalkMessageResponse {
    private String message;
    private String sender;
    private Long contentId;
    private Long messageId;
    private Long userId;

    @Builder
    private TalkMessageResponse(String message, String sender, Long contentId, Long messageId, Long userId) {
        this.message = message;
        this.sender = sender;
        this.contentId = contentId;
        this.messageId = messageId;
        this.userId = userId;
    }

    public static TalkMessageResponse of(TalkMessage message) {
        return TalkMessageResponse
                .builder()
                .message(message.getMessage())
                .sender(message.getSender())
                .contentId(message.getContent().getId())
                .userId(message.getUser().getId())
                .messageId(message.getId())
                .build();
    }

    public static List<TalkMessageResponse> dtoListOf(List<TalkMessage> messageList) {
        return messageList.stream()
            .map(TalkMessageResponse::of)
            .toList();
    }
}
