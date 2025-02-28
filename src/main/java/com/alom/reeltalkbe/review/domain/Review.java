package com.alom.reeltalkbe.review.domain;

import com.alom.reeltalkbe.comment.domain.Comment;
import com.alom.reeltalkbe.common.BaseEntity;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.image.domain.Image;
import com.alom.reeltalkbe.review.domain.reviewLike.LikeType;
import com.alom.reeltalkbe.review.domain.reviewLike.ReviewLike;
import com.alom.reeltalkbe.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @Lob
    private String overview;
    private String videoPath;
    private Long duration;
    private String title;


    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> reviewLikes = new ArrayList<>();



    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    @Builder
    public Review(Content content, User user, Image image, String videoPath, String overview,Long duration, String title) {
        this.content = content;
        this.user = user;
        this.image = image;
        this.videoPath = videoPath;
        this.overview = overview;
        this.duration = duration;
        this.title=title;
    }



    /**
     * 리뷰 수정 메서드
     */
    public void updateVideoAndTitle(String videoPath, String title) {
        if (videoPath != null && !videoPath.isEmpty()) this.videoPath = videoPath;
        if (title != null && !title.isEmpty()) this.title = title;
    }

    public void updateOverview(String overview) {
        if (overview != null && !overview.isEmpty()) this.overview=overview;
    }

    /**
     * 직접 좋아요/싫어요 개수 조회
     */
    public Long getLikeCount() {
        return reviewLikes.stream().filter(like -> like.getLikeType() == LikeType.LIKE).count();
    }

    public Long getHateCount() {
        return reviewLikes.stream().filter(like -> like.getLikeType() == LikeType.HATE).count();
    }


}
