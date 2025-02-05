package com.alom.reeltalkbe.content.domain;

import com.alom.reeltalkbe.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "rating",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "content_id"})
        })
public class Rating extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    */

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    private Integer ratingValue; // 1~5점

    public Rating() {}

    // todo : 빌더패턴 user 추가
    @Builder
    public Rating(Content content, Integer ratingValue) {
        this.content = content;
        this.ratingValue = ratingValue;
    }

}
