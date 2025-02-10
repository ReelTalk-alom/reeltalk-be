package com.alom.reeltalkbe.user.controller;

import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.image.service.ImageUploadService;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.dto.JoinDto;
import com.alom.reeltalkbe.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User registerUser(@RequestBody JoinDto joinDto) {
        return userService.registerUser(joinDto);
    }

    // admin role test
    @GetMapping("/admin")
    public String admin() {
        return "admin";
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
