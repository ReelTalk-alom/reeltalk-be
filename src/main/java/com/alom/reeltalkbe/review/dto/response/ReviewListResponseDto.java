package com.alom.reeltalkbe.review.dto.response;


import com.alom.reeltalkbe.content.domain.Content;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;


import java.time.LocalDate;
import java.util.List;

@Getter
public class ReviewListResponseDto {

    private final Long contentId;

    private final String title;
    private final String genre;
    private final String nation;
    private final LocalDate publishedAt;
    private final Double ratingAverage;

    private final List<ReviewResponseDto> result;

    private final int totalPages;  // 전체 페이지 수
    private final long totalElements;  // 전체 데이터 개수
    private final int currentPage;  // 현재 페이지 번호

    @Builder
    private ReviewListResponseDto(Long contentId, String title, String genre, String nation, LocalDate publishedAt,
                                  Double ratingAverage, List<ReviewResponseDto> result, int totalPages,
                                  long totalElements, int currentPage) {
        this.contentId = contentId;
        this.title = title;
        this.genre = genre;
        this.nation = nation;
        this.publishedAt = publishedAt;
        this.ratingAverage = ratingAverage;
        this.result = result;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
    }

    // ✅ Content 엔티티와 Page 객체를 받아서 DTO로 변환하는 정적 메서드
    public static ReviewListResponseDto fromEntity(Content content, Page<ReviewResponseDto> reviewPage) {
        return ReviewListResponseDto.builder()
                .contentId(content.getId())
                .title(content.getEnTitle())
                .genre(content.getGenres().toString())
                .nation(content.getCountry())
                .publishedAt(content.getReleaseDate())
                .ratingAverage(content.getRatingAverage())
                .result(reviewPage.getContent())  //  변환된 리뷰 리스트 사용
                .totalPages(reviewPage.getTotalPages())
                .totalElements(reviewPage.getTotalElements())
                .currentPage(reviewPage.getNumber())
                .build();
    }
}
