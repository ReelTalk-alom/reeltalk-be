package com.alom.reeltalkbe.review.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRegisterRequestDto {

        private Long contentId;
        private Long userId;

        private String imageUrl;
        private String url;
        private String description;
        private Float rating;
}
