package com.alom.reeltalkbe.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    /*
    private Long id;
    private String username;
    private String imageUrl;
    private String description;
*/

    private Long user_id; //사용자 아이디
    private String user_name; //사용자 이름
    private String user_img; //프로필 이미지 주소
    private String bio; //한줄 소개

}
