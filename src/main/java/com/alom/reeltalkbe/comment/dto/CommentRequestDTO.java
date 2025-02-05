package com.alom.reeltalkbe.comment.dto;

import com.alom.reeltalkbe.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {

    private String content;
    private int rating;

    public CommentRequestDTO(Comment comment) {
        this.content = comment.getContent();
        this.rating = comment.getRating();
    }
}
