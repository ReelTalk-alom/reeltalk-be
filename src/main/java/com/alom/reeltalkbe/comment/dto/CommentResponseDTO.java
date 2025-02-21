package com.alom.reeltalkbe.comment.dto;


import com.alom.reeltalkbe.comment.entity.Comment;
import com.alom.reeltalkbe.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String user;
    private String userImg;
    private Review review;
    private String content;
    private int likeCount;


    public CommentResponseDTO(Comment comment) {
        id = comment.getId();
        user = comment.getUser().getUsername();
        userImg = comment.getUser().getField();
        review = comment.getReview();
        content = comment.getContent();
        likeCount = comment.getLikeCount();
    }
}
