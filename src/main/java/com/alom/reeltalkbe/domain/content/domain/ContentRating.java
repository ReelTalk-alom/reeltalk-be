package com.alom.reeltalkbe.domain.content.domain;

import com.alom.reeltalkbe.common.BaseEntity;
import com.alom.reeltalkbe.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "rating",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "content_id"})
        })
public class ContentRating extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    private Integer ratingValue; // 1~5점

    public ContentRating() {}

    @Builder
    public ContentRating(User user, Content content, Integer ratingValue) {
        this.user = user;
        this.content = content;
        this.ratingValue = ratingValue;
    }

}
