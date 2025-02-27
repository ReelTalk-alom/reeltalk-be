package com.alom.reeltalkbe.comment.dto;

import com.alom.reeltalkbe.comment.domain.Like;

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
