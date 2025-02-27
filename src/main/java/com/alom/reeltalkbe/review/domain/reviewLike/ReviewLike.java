package com.alom.reeltalkbe.review.domain.reviewLike;

import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.domain.reviewLike.LikeType;
import com.alom.reeltalkbe.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "review_like",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "review_id"})
        })
@NoArgsConstructor
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;


    @Enumerated(EnumType.STRING)
    private LikeType likeType; // LIKE or HATE

    @Builder
    public ReviewLike(User user, Review review, LikeType likeType) {
        this.user = user;
        this.review = review;
        this.likeType = likeType;
    }

    public void changeLikeType(LikeType likeType) {
        if (this.likeType != likeType) {
            this.likeType = likeType;
        }
    }
}
