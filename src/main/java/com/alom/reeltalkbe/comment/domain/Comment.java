package com.alom.reeltalkbe.comment.domain;

import com.alom.reeltalkbe.common.BaseEntity;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private int likeCount;

    private String content;




    public void updateContnet(String content) {
        this.content = content;
    }
    public void updateLikeCount(int likeCount){
        this.likeCount = likeCount;
    }



}
