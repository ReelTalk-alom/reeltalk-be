package com.alom.reeltalkbe.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequestDto {

    private Long reviewId;

    private String description;
    private String url;
    private String imageUrl;
    private Float rating;
}
