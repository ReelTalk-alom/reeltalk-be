package com.alom.reeltalkbe.comment.dto;


import com.alom.reeltalkbe.comment.entity.Comment;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.user.domain.User;
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
    private User user;
    private Review review;
    private String content;


    public CommentResponseDTO(Comment comment) {
        id = comment.getId();
        user = comment.getUser();
        review = comment.getReview();
        content = comment.getContent();
    }
}
