package com.alom.reeltalkbe.review.dto.response.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSummaryDto {

    private Long commentId;

    private String userName;
    private String userImage;
    private String title;
    private String createdAt;

//    private int likeCount;
}
