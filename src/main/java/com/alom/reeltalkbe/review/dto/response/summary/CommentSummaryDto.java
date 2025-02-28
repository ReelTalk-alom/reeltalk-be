package com.alom.reeltalkbe.review.dto.response.summary;

import com.alom.reeltalkbe.comment.domain.Comment;
import lombok.*;


@Getter
public class CommentSummaryDto {

    private final Long userId;
    private final String userName;
    private final String createdAt;

    private final int likeCount;

    @Builder
    private CommentSummaryDto(Long userId, String userName, String createdAt, int likeCount) {
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
    }

    public static CommentSummaryDto fromEntity(Comment comment) {
        return CommentSummaryDto.builder()
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getUsername())
                .createdAt(comment.getCreatedAt().toString())
                .likeCount(comment.getLikeCount())
                .build();
    }
}
