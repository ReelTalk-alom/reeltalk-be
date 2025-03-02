package com.alom.reeltalkbe.content.controller;


import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.dto.RatingRequest;
import com.alom.reeltalkbe.content.service.ContentRatingService;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/contents/{contentId}")
@RestController
@RequiredArgsConstructor
public class ContentRatingController {

    private final ContentRatingService contentRatingService;


    // 컨텐츠 평점 생성
    @PostMapping
    public BaseResponse<Content> evaluateContentRating(@PathVariable Long contentId,
                                                       @RequestBody RatingRequest ratingRequest,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        // RestController = (RequestBody XXX )ResponseBody + Controller
        return new BaseResponse<>(contentRatingService
                .addRating(contentId, userDetails.getUserId(), ratingRequest));
    }

    // 컨텐츠 평점 삭제
    @DeleteMapping
    public BaseResponse<Content> deleteContentRating(@PathVariable Long contentId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(contentRatingService
                .deleteRating(contentId, userDetails.getUserId()));
    }
}
