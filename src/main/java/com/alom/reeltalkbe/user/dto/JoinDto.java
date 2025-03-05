package com.alom.reeltalkbe.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDto {
    private String username;
    private String password;
    private String email;
    private String description;  // 추가
}
