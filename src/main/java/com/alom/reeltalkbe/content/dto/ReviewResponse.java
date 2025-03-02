package com.alom.reeltalkbe.content.dto;

import com.alom.reeltalkbe.image.domain.Image;
import com.alom.reeltalkbe.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class ReviewResponse {

    private Long id;
    @JsonProperty("content_id")
    private Long contentId;
    @JsonProperty("user_id")
    private Long userId;
    private Image image;
    private String overview;
    @JsonProperty("video_path")
    private String videoPath;
    private Long duration;
    private String title;

    @JsonProperty("like_count")
    private Long likeCount;
    @JsonProperty("hate_count")
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
