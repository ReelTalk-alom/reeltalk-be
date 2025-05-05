package com.alom.reeltalkbe.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {


    private Long contentId;

    private String thumbnail;
    private String overview;
    private String videoPath;
}
