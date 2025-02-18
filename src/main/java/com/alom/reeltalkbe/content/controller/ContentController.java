package com.alom.reeltalkbe.content.controller;


import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.dto.RatingDto;
import com.alom.reeltalkbe.content.service.ContentService;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    // 메인화면 - 넷플릭스 처럼
    @GetMapping("/browse/tab")              // todo : type enum 만들기
    public BaseResponse<String> getContents(@RequestParam String type) {
        contentService.findPopularContentImages();
        // todo : 포스터+백그라운드(가로) 이미지 두개 추가
        return new BaseResponse<>("흠, 어떡하쥥");
    }
    // todo : 영화, 시리즈 탭 영화 + 리뷰 여러 개 반황
    // 컨텐츠 세부 정보 조회
    @GetMapping("/contents/{contentId}")
    public BaseResponse<Content> getContentInfo(@PathVariable Long contentId) {
        return new BaseResponse<>(contentService
                .findContentById(contentId));
    }

    // 컨텐츠 평점 생성
    @PostMapping("/contents/{contentId}")
    public BaseResponse<Content> evaluateContentRating(@PathVariable Long contentId,
                                                       @RequestBody RatingDto ratingDto,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        // RestController = (RequestBody XXX )ResponseBody + Controller
        return new BaseResponse<>(contentService
                .addRating(contentId, userDetails.getUserId(), ratingDto));
    }

    // 컨텐츠 평점 삭제
    @DeleteMapping("/contents/{contentId}")
    public BaseResponse<Content> deleteContentRating(@PathVariable Long contentId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        return new BaseResponse<>(contentService
                .deleteRating(contentId, userDetails.getUserId()));
    }


    // --------------- 테스트용 메서드 ----------------------
    // 컨텐츠 평점 생성
    @GetMapping("/contents")
    public BaseResponse<Content> addContent() {
        return new BaseResponse<>(contentService.addContent(new Content()));
    }

}
