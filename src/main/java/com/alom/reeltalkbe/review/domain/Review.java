package com.alom.reeltalkbe.review.domain;

import com.alom.reeltalkbe.comment.entity.Comment;
import com.alom.reeltalkbe.common.BaseEntity;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.image.domain.Image;
import com.alom.reeltalkbe.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @Column(name ="review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @Lob
    private String description;
    private String url;

    private Float rating;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    @Builder
    public Review(Content content, User user, Image image, String url, String description, Float rating) {
        this.content = content;
        this.user = user;
        this.image = image;
        this.url = url;
        this.description = description;
        this.rating = rating;
    }

    /**
     * 리뷰 수정 메서드
     */
    public void updateReview(String url, String description, Float rating) {
        this.url = url;
        this.description = description;
        this.rating = rating;
    }
}
