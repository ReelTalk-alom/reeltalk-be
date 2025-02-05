package com.alom.reeltalkbe.content.controller;


import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.dto.RatingDto;
import com.alom.reeltalkbe.content.service.ContentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    // 메인화면 - 넷플릭스 처럼
    @GetMapping("/browse/tab")              // todo : type enum 만들기
    public BaseResponse<String> getContents(@RequestParam String type) {
        contentService.findPopularContentImages();
        return new BaseResponse<>("흠, 어떡하쥥");
    }

    // 컨텐츠 세부 정보 조회
    @GetMapping("/contents/{content_id}")
    public BaseResponse<Content> getContentInfo(@PathVariable Long content_id) {
        return new BaseResponse<>(contentService
                .findContentById(content_id));
    }

    // 컨텐츠 평점 생성
    @PostMapping("/contents/{content_id}")
    public BaseResponse<Content> evaluateContentRating(@PathVariable Long content_id,
                                                       RatingDto ratingDto) {
        return new BaseResponse<>(contentService
                .addRating(content_id, ratingDto));
    }

    // 컨텐츠 평점 삭제
    @DeleteMapping("/contents/{content_id}")
    public BaseResponse<Content> deleteContentRating(@PathVariable Long content_id,
                                                     RatingDto ratingDto) {
        return new BaseResponse<>(contentService
                .deleteRating(content_id, ratingDto.getUser_id()));
    }


}
