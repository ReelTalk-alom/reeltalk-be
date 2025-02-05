package com.alom.reeltalkbe.controller;

import com.alom.reeltalkbe.domain.User;
import com.alom.reeltalkbe.dto.JoinDto;
import com.alom.reeltalkbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
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

}
