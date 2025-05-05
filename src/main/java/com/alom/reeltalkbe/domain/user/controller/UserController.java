package com.alom.reeltalkbe.domain.user.controller;

import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.domain.user.dto.CustomUserDetails;
import com.alom.reeltalkbe.domain.user.dto.JoinDto;
import com.alom.reeltalkbe.domain.user.domain.User;
import com.alom.reeltalkbe.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 인증, 인가 관련 API")
public class UserController {

    private final UserService userService;

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
}
