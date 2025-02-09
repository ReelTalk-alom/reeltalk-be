package com.alom.reeltalkbe.review.controller;

import com.alom.reeltalkbe.review.dto.ReviewRatingDto;
import com.alom.reeltalkbe.review.service.ReviewRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewRatingController {

    private final ReviewRatingService reviewRatingService;

    /**
     * 리뷰 평점 등록 or 수정
     */
    @PostMapping("/rate")
    public ResponseEntity<String> rateReview(@RequestBody ReviewRatingDto reviewRatingDto) {
        reviewRatingService.rateReview(reviewRatingDto);
        return ResponseEntity.ok("평점이 등록되었습니다.");
    }
}