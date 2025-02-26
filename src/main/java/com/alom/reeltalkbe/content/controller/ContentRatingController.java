package com.alom.reeltalkbe.content.controller;


import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.dto.RatingDto;
import com.alom.reeltalkbe.content.service.ContentRatingService;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ContentRatingController {

    private final ContentRatingService contentRatingService;


    // 컨텐츠 평점 생성
    @PostMapping("/contents/{contentId}")
    public BaseResponse<Content> evaluateContentRating(@PathVariable Long contentId,
                                                       @RequestBody RatingDto ratingDto,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        // RestController = (RequestBody XXX )ResponseBody + Controller
        return new BaseResponse<>(contentRatingService
                .addRating(contentId, userDetails.getUserId(), ratingDto));
    }

    // 컨텐츠 평점 삭제
    @DeleteMapping("/contents/{contentId}")
    public BaseResponse<Content> deleteContentRating(@PathVariable Long contentId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(contentRatingService
                .deleteRating(contentId, userDetails.getUserId()));
    }
}
