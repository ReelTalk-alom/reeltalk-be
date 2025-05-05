package com.alom.reeltalkbe.domain.comment.dto;

import com.alom.reeltalkbe.domain.comment.domain.Like;

public class LikeDTO {

    private Long id;
    private String user;
    private Long commentId;

    public LikeDTO(Like like){
        id = like.getId();
        user = like.getUser().getUsername();
        commentId = like.getComment().getId();
    }

}
