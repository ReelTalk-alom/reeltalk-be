package com.alom.reeltalkbe.global;


import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.domain.content.domain.Content;
import com.alom.reeltalkbe.domain.content.dto.TMDB.TMDBMovieDetailsRequest;
import com.alom.reeltalkbe.domain.content.dto.TMDB.TMDBSeriesDetailsRequest;
import com.alom.reeltalkbe.domain.content.service.ContentService;
import com.alom.reeltalkbe.domain.review.dto.response.ReviewListResponseDto;
import com.alom.reeltalkbe.domain.review.dto.response.ReviewResponseDto;
import com.alom.reeltalkbe.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "백엔드 개발자용 Test", description = "Test 관련 API")
@RequiredArgsConstructor
public class TestController {

  private final ContentService contentService;
  private final ReviewService reviewService;

  // admin role test
  @Operation(summary = "개발자용 어드민 테스트", description = "관리자 인증시 admin을 반환합니다.")
  @GetMapping("/admin/test")
  public String admin() {
    return "admin";
  }

  // 컨텐츠 생성
  @PostMapping("/contents")
  public BaseResponse<Content> addContents(@RequestBody TMDBMovieDetailsRequest request) {
    return new BaseResponse<>(contentService.addContent(new Content(request)));
  }

  // 시리즈 컨텐츠 생성
  @PostMapping("/contents/series")
  public BaseResponse<Content> addSeriesContents(@RequestBody TMDBSeriesDetailsRequest request) {
    return new BaseResponse<>(contentService.addContent(new Content(request)));
  }

  // 자동 최신화 기능 테스트
  @GetMapping("/series/test")
  public BaseResponse<String> getNewSeries() {
    return new BaseResponse<>(contentService.updateLatestSeries());
  }

  // 자동 최신화 기능 테스트
  @GetMapping("/movies/test")
  public BaseResponse<String> getNewMovies() {
    return new BaseResponse<>(contentService.updateLatestMovies());
  }

  @GetMapping("/sample")
  public BaseResponse<List<ReviewListResponseDto>> getSampleReviews(){
    List<ReviewListResponseDto> reviewListResponseDtos = reviewService.reviewSample();
    return new BaseResponse<>(reviewListResponseDtos);
  }
  @GetMapping("/random")
  public BaseResponse<List<ReviewResponseDto>> getRandomReviews(){
    List<ReviewResponseDto> reviewResponseDtos = reviewService.randomReview();
    return new BaseResponse<>(reviewResponseDtos);
  }
}
