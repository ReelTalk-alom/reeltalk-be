package com.alom.reeltalkbe.review.dto.request;

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

}
