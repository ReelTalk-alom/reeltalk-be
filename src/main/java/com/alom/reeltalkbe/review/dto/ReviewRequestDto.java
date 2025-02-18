package com.alom.reeltalkbe.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {


    private Long contentId;
    private Long imageId;

    private String description;
    private String url;
}
