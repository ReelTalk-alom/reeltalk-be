package com.alom.reeltalkbe.review.dto.response;

import com.alom.reeltalkbe.review.dto.response.summary.ReviewSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponseDto {

    private Long contentId;

    private String title;
    private String genre;
    private String nation;
    private String releasedAt;
    private Double ratingAverage;

    private List<ReviewSummaryDto> result;

    private int totalPages;  // 전체 페이지 수
    private long totalElements;  // 전체 데이터 개수
    private int currentPage;  // 현재 페이지 번호
}
