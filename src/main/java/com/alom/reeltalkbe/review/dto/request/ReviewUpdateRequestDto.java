package com.alom.reeltalkbe.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequestDto {

    private String description;
    private String url;
    private String imageUrl;
    private Float rating;
}
