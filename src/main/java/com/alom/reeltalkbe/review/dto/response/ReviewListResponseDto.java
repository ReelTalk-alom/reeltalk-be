package com.alom.reeltalkbe.review.dto.response;

import com.alom.reeltalkbe.review.dto.ReviewSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponseDto {
    private List<ReviewSummaryDto> result;
}
