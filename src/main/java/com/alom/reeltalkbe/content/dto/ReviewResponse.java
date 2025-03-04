package com.alom.reeltalkbe.content.dto;

import com.alom.reeltalkbe.image.domain.Image;
import com.alom.reeltalkbe.review.domain.Review;
import lombok.Builder;
import lombok.Data;

@Data
public class ReviewResponse {

    private Long id;
    private Long contentId;
    private Long userId;
    private Image image;
    private String overview;
    private String videoPath;
    private Long duration;
    private String title;

    private Long likeCount;
    private Long hateCount;

    @Builder
    private ReviewResponse(Long id, Long contentId, Long userId, Image image, String overview, String videoPath, Long duration, String title, Long likeCount, Long hateCount) {
        this.id = id;
        this.contentId = contentId;
        this.userId = userId;
        this.image = image;
        this.overview = overview;
        this.videoPath = videoPath;
        this.duration = duration;
        this.title = title;
        this.likeCount = likeCount;
        this.hateCount = hateCount;
    }

    public static ReviewResponse of(Review review) {
        return ReviewResponse.builder()
              .id(review.getId())
              .contentId(review.getContent().getId())
              .userId(review.getUser().getId())
              .image(review.getImage())
              .duration(review.getDuration())
              .videoPath(review.getVideoPath())
              .overview(review.getOverview())
              .title(review.getTitle())
              .likeCount(review.getLikeCount())
              .hateCount(review.getHateCount())
              .build();
    }
}
