package com.alom.reeltalkbe.review.domain;


import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "review_rating",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "review_id"})
        })
public class ReviewRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private Integer ratingValue; // 1~5점

    public ReviewRating() {}

    // todo : 빌더패턴 user 추가
    @Builder
    public ReviewRating(User user, Review review, Integer ratingValue) {
        this.user = user;
        this.review =review;
        this.ratingValue = ratingValue;

    }

}
