package com.alom.reeltalkbe.user.controller;

import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.image.service.ImageUploadService;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import com.alom.reeltalkbe.user.dto.JoinDto;
import com.alom.reeltalkbe.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, ImageUploadService imageUploadService) {
        this.userService = userService;
    }


    // 회원가입 API
    @PostMapping("/signup")
    public BaseResponse<User> registerUser(@RequestBody JoinDto joinDto) {
        return new BaseResponse<>(userService.registerUser(joinDto));
    }

    // 회원 탈퇴
    @DeleteMapping("/mypage")
    public BaseResponse<String> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response) {
        userService.deleteUser(userDetails.getUserId(), response);
        return new BaseResponse<>("회원 탈퇴가 완료되었습니다.");
    }

    // 프로필 이미지 등록
    @PostMapping("/mypage/image")
    public BaseResponse<String> uploadProfileImage(MultipartFile multipartFile) {
        String url = userService.uploadProfileImage(multipartFile);
        return new BaseResponse<>(url);
    }

    // 프로필 이미지 삭제
    @DeleteMapping("/mypage/image")
    public BaseResponse<String> deleteProfileImage() {
        userService.deleteProfileImage();
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    // 프로필 이미지 조회
    @GetMapping("/mypage/image")
    public BaseResponse<String> getProfileImage() {
        String url = userService.getProfileImage();
        return new BaseResponse<>(url);
    }

    // admin role test
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
