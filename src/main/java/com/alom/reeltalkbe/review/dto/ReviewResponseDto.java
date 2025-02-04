package com.alom.reeltalkbe.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;
    private Long contentId;
    private Long imageId;

    private String url;
    private String description;
    private Float rating;
}
