package com.alom.reeltalkbe.review.dto.response;

import com.alom.reeltalkbe.review.domain.Review;
import lombok.*;


@Getter
public class ReviewResponseDto {

    private final Long reviewId;
    private final String title;

    private final Long authorId;
    private final String author;
    private final String overview;
    private final String videoPath;

    private final String publishedAt;

    private final String thumbnail;
    private final Long likeCount;
    private final Long hateCount;

    @Builder
    private ReviewResponseDto(Long reviewId, String title, Long authorId, String author, String overview,
                              String videoPath, String publishedAt, String thumbnail, Long likeCount, Long hateCount) {
        this.reviewId = reviewId;
        this.title = title;
        this.authorId = authorId;
        this.author = author;
        this.overview = overview;
        this.videoPath = videoPath;
        this.publishedAt= publishedAt;
        this.thumbnail = thumbnail;
        this.likeCount = likeCount;
        this.hateCount = hateCount;
    }

    //  DTO 내부에서 엔티티를 변환하는 정적 메서드 추가
    public static ReviewResponseDto fromEntity(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .authorId(review.getUser().getId())
                .author(review.getUser().getUsername())
                .overview(review.getOverview())
                .videoPath(review.getVideoPath())
                .publishedAt(review.getCreatedAt().toString())
                .thumbnail(review.getImage().getUrl())
                .likeCount(review.getLikeCount())
                .hateCount(review.getHateCount())
                .build();
    }
}
