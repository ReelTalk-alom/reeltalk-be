package com.alom.reeltalkbe.review.dto.response.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSummaryDto {

    private Long userId;
    private String userName;
    private String ImageUrl;
    private String createdAt;

    private int likeCount;
}
