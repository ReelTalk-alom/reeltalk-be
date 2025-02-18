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
    private Long imageId;
    private Long userId;

    private String url;
    private String description;
    private double ratingAverage;

}
