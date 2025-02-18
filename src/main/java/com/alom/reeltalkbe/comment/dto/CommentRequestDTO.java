package com.alom.reeltalkbe.comment.dto;

import com.alom.reeltalkbe.comment.domain.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {

    private String content;


    public CommentRequestDTO(Comment comment) {
        this.content = comment.getContent();
    }
}
