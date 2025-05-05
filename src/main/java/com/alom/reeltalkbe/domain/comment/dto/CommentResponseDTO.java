package com.alom.reeltalkbe.domain.comment.dto;


import com.alom.reeltalkbe.domain.comment.domain.Comment;
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
    private Long reviewId;
    private String content;
    private int likeCount;


    public CommentResponseDTO(Comment comment) {
        id = comment.getId();
        user = comment.getUser().getUsername();
        userImg = comment.getUser().getImageUrl();
        reviewId = comment.getReview().getId();
        content = comment.getContent();
        likeCount = comment.getLikeCount();
    }
}
