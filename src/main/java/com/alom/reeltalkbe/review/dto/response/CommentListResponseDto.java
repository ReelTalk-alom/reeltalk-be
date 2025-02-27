package com.alom.reeltalkbe.review.dto.response;

import com.alom.reeltalkbe.review.dto.response.summary.CommentSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResponseDto {

    private Long reviewId;
    private String title;

    private String userName;
    private Long userId;

    private String overview;
    private String videoPath;
    private String publishedAt;
    private Long duration;
    private String imageUrl;
    private Long likeCount;
    private Long hateCount;


    private List<CommentSummaryDto> comments;
}
