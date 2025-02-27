package com.alom.reeltalkbe.review.dto.response;

import com.alom.reeltalkbe.review.dto.response.summary.CommentSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;
    private Long contentId;

    private Long userId;

    private String viewPath;
    private String overview;

    private Long duration;
    private String imageUrl;
    private Long likeCount;
    private Long hateCount;
}
