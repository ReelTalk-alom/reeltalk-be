package com.alom.reeltalkbe.domain.content.controller;


import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.domain.content.dto.ContentDetailsResponse;
import com.alom.reeltalkbe.domain.content.dto.MovieTabResponse;
import com.alom.reeltalkbe.domain.content.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Content", description = "컨텐츠 관련 API")
public class ContentController {
    private final ContentService contentService;

    // 메인화면 - 컬렉션 리스트 여러개
//    @GetMapping("/browse")
//    @Operation(summary = "컨텐츠 전체 목록 조회", description = "평점순 또는 최신순으로 영화 리스트를 반환합니다.")
//    public BaseResponse<List<Content>> getContents(@RequestParam("sort") String sort) {
//        return new BaseResponse<>(contentService.findAll());
//    }


    // todo: 검색 기능 - 한자 칠때마다 바슷한 결과 나오는거
    // todo: pageable로 줄건지, 몇개씩 줄건지 고민
    // 영화 탭
    @GetMapping("/movies")
    @Operation(summary = "영화 전체 목록 조회", description = "movies?sort= 방식의 요청을 받아, 평점순 또는 최신순으로 영화 리스트를 반환합니다.")
    public BaseResponse<List<MovieTabResponse>> getMoviesSortBy(@RequestParam("sort") String sort) {
        return new BaseResponse<>(contentService.findMoviesAndReviewsSortBy(sort));
    }

    // 시리즈 탭
//    @GetMapping("/series")
//    public BaseResponse<List<SeriesTabResponse>> getSeriesSortBy(@RequestParam("sort") String sort) {
//        return new BaseResponse<>(contentService.findSeriesAndReviewsSortBy(sort));
//    }

    // 컨텐츠 세부 정보 조회
    @GetMapping("/contents/{contentId}")
    @Operation(summary = "영화 상세 정보 조회", description = "영화 상세 정보를 반환합니다.")
    public BaseResponse<ContentDetailsResponse> getContentDetails(@PathVariable Long contentId) {
        return new BaseResponse<>(contentService
                .findContentDetailsByContentId(contentId));
    }

//    @GetMapping("/series")
//    public BaseResponse<?> getSeriesTopRated(@RequestParam("sort") String filter) {
//        return new BaseResponse<>(contentService.findSeriesWithReviewsByFilter(filter));
//    }
}
