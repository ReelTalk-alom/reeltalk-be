package com.alom.reeltalkbe.domain.user.controller;

import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.exception.BaseResponseStatus;
import com.alom.reeltalkbe.domain.user.dto.CustomUserDetails;
import com.alom.reeltalkbe.domain.user.dto.MyPageResponseDto;
import com.alom.reeltalkbe.domain.user.dto.MyPageUpdateRequestDto;
import com.alom.reeltalkbe.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users/mypage")
@RequiredArgsConstructor
@Tag(name = "User, Mypage", description = "마이페이지 관련 API")
public class MyPageController {

    private final UserService userService;

    /**
     * 내 마이페이지 조회 (로그인한 사용자)
     */
//    @GetMapping
//    public BaseResponse<MyPageResponseDto> getMyPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        Long userId = userDetails.getUserId();
//        MyPageResponseDto myPageData = userService.getMyPageInfo(userId);
//        return new BaseResponse<>(myPageData);
//    }

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
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

  // 프로필 이미지 등록
  @PostMapping("/image")
  public BaseResponse<String> uploadProfileImage(MultipartFile multipartFile) {
    String url = userService.uploadProfileImage(multipartFile);
    return new BaseResponse<>(url);
  }

  // 프로필 이미지 삭제
  @DeleteMapping("/image")
  public BaseResponse<String> deleteProfileImage() {
    userService.deleteProfileImage();
    return new BaseResponse<>(BaseResponseStatus.SUCCESS);
  }

  // 프로필 이미지 조회
  @GetMapping("/image")
  public BaseResponse<String> getProfileImage() {
    String url = userService.getProfileImage();
    return new BaseResponse<>(url);
  }
}
