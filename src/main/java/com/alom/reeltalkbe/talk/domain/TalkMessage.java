package com.alom.reeltalkbe.talk.domain;


import com.alom.reeltalkbe.common.BaseEntity;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.talk.dto.TalkMessageRequest;
import com.alom.reeltalkbe.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;


@Getter
@Entity
@Table(name = "talk_message")
public class TalkMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LAZY 로딩시, 객체를 반환해야할때 ByteBuddyInterceptor 오류 생김
    // JSON 직렬화 시도 시 프록시 객체가 Jackson 에 노출되기 때문
    // DTO 변환으로 해결 !
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String sender;
    private String message;

    @Builder
    private TalkMessage(Long id, Content content, User user, String sender, String message) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.message = message;
        this.sender = sender;   // todo: 닉네임으로 변경? 고민
    }

    public TalkMessage() {}

    public static TalkMessage of(TalkMessageRequest dto, Content content) {
        return TalkMessage.builder()
                .message(dto.getMessage())
                .content(content)
                .sender(dto.getSender())
                .user(dto.getUser())
                .build();
    }
    public void updateMessage(TalkMessageRequest dto) {
        this.message = dto.getMessage();
    }
}
