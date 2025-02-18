package com.alom.reeltalkbe.comment.domain;

import com.alom.reeltalkbe.common.BaseEntity;
import com.alom.reeltalkbe.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "comment_id")
    Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
