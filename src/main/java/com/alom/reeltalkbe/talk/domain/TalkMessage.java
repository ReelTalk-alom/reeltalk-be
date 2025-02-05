package com.alom.reeltalkbe.talk.domain;


import com.alom.reeltalkbe.common.BaseEntity;
import com.alom.reeltalkbe.content.domain.Content;
import jakarta.persistence.*;

@Entity
@Table(name = "talk_message")
public class TalkMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
    /*
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
    */
    private String sender;
    private String message;


}
