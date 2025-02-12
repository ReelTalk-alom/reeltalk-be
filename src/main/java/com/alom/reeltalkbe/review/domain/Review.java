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

    private Double ratingCount=0.0;
    private Double ratingSum=0.0;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    @Builder
    public Review(Content content, User user, Image image, String url, String description) {
        this.content = content;
        this.user = user;
        this.image = image;
        this.url = url;
        this.description = description;
    }

    /**
     개별 평점 추가 (ReviewRating 추가)
     */
    public void addRating(double ratingValue) {
        this.ratingSum += ratingValue;
        this.ratingCount++;
    }

    /**
     * 개별 평점 제거 (ReviewRating 삭제)
     */
    public void removeRating(double ratingValue) {

        this.ratingSum -= ratingValue;
        this.ratingCount--;
    }

    /**
     * 리뷰 수정 메서드
     */
    public void updateReview(String url, String description) {
        this.url = url;
        this.description = description;
    }

    /**
     *  평균 평점 계산
     */
    public double getRatingAverage() {
        return (ratingCount == 0) ? 0.0 : (double) ratingSum / ratingCount;
    }


}
