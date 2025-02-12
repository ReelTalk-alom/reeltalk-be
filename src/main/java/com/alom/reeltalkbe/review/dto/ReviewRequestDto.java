package com.alom.reeltalkbe.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    private Long reviewId;
    private Long contentId;
    private Long userId;

    private String description;
    private String url;
    private String imageUrl;

}
