package com.alom.reeltalkbe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPageUpdateRequestDto {
    private String username;   // 변경할 닉네임
    private String description; // 변경할 자기소개

    // private String imageUrl; // 프로필 이미지 변경하기 (나중에)
}
