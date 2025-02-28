package com.alom.reeltalkbe.review.dto.response;

import com.alom.reeltalkbe.comment.domain.Comment;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.dto.response.summary.CommentSummaryDto;
import lombok.*;

import java.util.List;

@Getter
public class CommentListResponseDto {

    private final Long reviewId;
    private final String title;

    private final String author;
    private final Long authorId;

    private final String overview;
    private final String videoPath;
    private final String publishedAt;
    private final Long duration;
    private final String imageUrl;
    private final Long likeCount;
    private final Long hateCount;

    private final List<CommentSummaryDto> comments;

    @Builder
    private CommentListResponseDto(Long reviewId, String title, String author, Long authorId, String overview,
                                   String videoPath, String publishedAt, Long duration, String imageUrl,
                                   Long likeCount, Long hateCount, List<CommentSummaryDto> comments) {
        this.reviewId = reviewId;
        this.title = title;
        this.author = author;
        this.authorId = authorId;
        this.overview = overview;
        this.videoPath = videoPath;
        this.publishedAt = publishedAt;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.hateCount = hateCount;
        this.comments = comments != null ? comments : List.of(); // 방어 코드 추가 (null 방지)
    }

    // ✅ 리뷰 엔티티에서 DTO로 변환하는 정적 메서드 추가
    public static CommentListResponseDto fromEntity(Review review, List<Comment> comments) {
        return CommentListResponseDto.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .author(review.getUser().getUsername())
                .authorId(review.getUser().getId())
                .overview(review.getOverview())
                .videoPath(review.getVideoPath())
                .publishedAt(review.getCreatedAt().toString())
                .duration(review.getDuration())
                .imageUrl(review.getImage().getUrl())
                .likeCount(review.getLikeCount())
                .hateCount(review.getHateCount())
                .comments(comments != null
                        ? comments.stream().map(CommentSummaryDto::fromEntity).toList() //  여기서 변환 수행
                        : List.of()) //  null 방어 코드 추가
                .build();
    }
}
