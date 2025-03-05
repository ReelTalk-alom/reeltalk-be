package com.alom.reeltalkbe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    private Long user_id;
    private String username;
    private String description;
    private String email;
    private String image_url;
    private List<MyPageReviewDto> best_reviews;  // 좋아요 많은 순
    private List<MyPageReviewDto> recent_reviews;  // 최신순
}
