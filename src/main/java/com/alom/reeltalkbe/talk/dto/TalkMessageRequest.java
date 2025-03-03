package com.alom.reeltalkbe.talk.dto;

import com.alom.reeltalkbe.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
// todo : @NotNull , @Valid, bindingResult 고민 후 dto 나눌지도?
public class TalkMessageRequest {
    private Long messageId;
    private Long contentId;
    private Long userId;
    private User user;
    private String sender;
    private String message;
}
