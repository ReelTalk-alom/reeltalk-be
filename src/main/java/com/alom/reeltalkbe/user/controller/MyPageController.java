package com.alom.reeltalkbe.user.controller;

import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.user.dto.MyPageResponseDto;
import com.alom.reeltalkbe.user.dto.MyPageUpdateRequestDto;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import com.alom.reeltalkbe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserService userService;

    /**
     * 내 마이페이지 조회 (로그인한 사용자)
     */
    @GetMapping
    public BaseResponse<MyPageResponseDto> getMyPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        MyPageResponseDto myPageData = userService.getMyPageInfo(userId);
        return new BaseResponse<>(myPageData);
    }

    /**
     *  다른 사용자의 마이페이지 조회
     */
    @GetMapping("/{userId}")
    public BaseResponse<MyPageResponseDto> getUserPage(@PathVariable Long userId) {
        MyPageResponseDto userPageData = userService.getMyPageInfo(userId);
        return new BaseResponse<>(userPageData);
    }

    /**
     * 마이페이지 닉네임,자기소개 변경
     */
    @PutMapping
    public BaseResponse<String> updateMyPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestBody MyPageUpdateRequestDto requestDto) {
        Long userId = userDetails.getUserId();
        userService.updateMyPage(userId, requestDto);
        return new BaseResponse<>("프로필이 성공적으로 변경되었습니다");
    }
}
