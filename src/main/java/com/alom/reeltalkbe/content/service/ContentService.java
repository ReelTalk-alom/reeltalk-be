package com.alom.reeltalkbe.content.service;

import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.domain.ContentType;
import com.alom.reeltalkbe.content.dto.ContentDetailsResponse;
import com.alom.reeltalkbe.content.dto.MovieTabResponse;
import com.alom.reeltalkbe.content.dto.ReviewResponse;
import com.alom.reeltalkbe.content.dto.SeriesTabResponse;
import com.alom.reeltalkbe.content.dto.TMDB.TMDBMovieDetailsRequest;
import com.alom.reeltalkbe.content.dto.TMDB.TMDBSeriesDetailsRequest;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.repository.ReviewRepository;
import com.alom.reeltalkbe.talk.domain.TalkMessage;
import com.alom.reeltalkbe.talk.repository.TalkMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableAsync  // 비동기 기능 활성화
public class ContentService {

    private final ContentRepository contentRepository;
    private final ReviewRepository reviewRepository;
    private final TalkMessageRepository talkMessageRepository;
    private final TMDBService tmdbService;

    //private final CharacterRepository characterRepository;
    // tmdb 이슈로 캐릭터는 고민해야함

    // 포스터 이미지와 링크만 가져오기?
    public List<Content> findAll() {
        return contentRepository.findAll();
    }


    @Transactional
    public ContentDetailsResponse findContentDetailsByContentId(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));

        List<ReviewResponse> reviews = reviewRepository.findTop10ByContentIdOrderByReviewLikesDesc(content.getId())
                .stream()
                .map(ReviewResponse::of)
                .toList();

        // todo : talkMessages 시간순으로 나오는지 보기
        List<TalkMessage> talkMessages = talkMessageRepository.findAllByContentId(content.getId());
        return ContentDetailsResponse.of(content, reviews, talkMessages);
    }

    public List<MovieTabResponse> findMoviesAndReviewsSortBy(String sort) {

        List<Content> contentList = new ArrayList<>();
        switch (sort) {
            case "release-date" ->          // 개봉일자 오름차순 반환
                    contentList = contentRepository.findTop10ByOrderByReleaseDateDesc();
            case "top-rated" ->             // 별점 내림차순 반환
                    contentList = contentRepository.findTop10ByOrderByRatingAverageDesc();
            case "now-playing" ->           // 오늘 이전에 개봉한 영화 리스트 인기순 반환
                    contentList = contentRepository.findTop10ByReleaseDateBetweenOrderByPopularityDesc(LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(1));
            case "up-coming" ->             // 오늘 이후에 개봉할 영화를 개봉일자 오름차순으로 반환
                    contentList = contentRepository.findTop10ByReleaseDateAfterOrderByReleaseDateAsc(LocalDate.now());
            default ->                      // 분류기준 잘못되면 예외 처리
                    throw new BaseException(BaseResponseStatus.INVALID_QUERY_PARAMETER);
        }

        List<Long> contentIds = contentList.stream()
                .map(Content::getId)
                .toList();

        List<Review> reviewList = reviewRepository
                .findTop10ByContentIdInOrderByReviewLikesDesc(contentIds);

        Map<Long, List<Review>> reviewsByContent = reviewList.stream()
                .collect(Collectors.groupingBy(review -> review.getContent().getId()));

        return contentList.stream()
                .map(content -> MovieTabResponse.of(
                        content,
                        reviewsByContent.getOrDefault(content.getId(), Collections.emptyList()))
                )
                .toList();
    }

    public List<SeriesTabResponse> findSeriesAndReviewsSortBy(String sort) {
        List<Content> contentList = new ArrayList<>();

        if (sort.equals("firstAirDate")) {
            contentList = contentRepository.findTop10ByContentTypeOrderByReleaseDateAsc(ContentType.SERIES);
        }

        List<Long> contentIds = contentList.stream()
                .map(Content::getId)
                .collect(Collectors.toList());

        List<Review> reviewList = reviewRepository.findTop10ByContentIdInOrderByReviewLikesDesc(contentIds);

        Map<Long, List<Review>> reviewsByContent = reviewList.stream()
                .collect(Collectors.groupingBy(review -> review.getContent().getId()));

        return contentList.stream()
                .map(content ->
                        SeriesTabResponse.of(content, reviewsByContent.getOrDefault(content.getId(), Collections.emptyList())))
                .toList();
    }

    public List<SeriesTabResponse> findSeriesWithReviewsByFilter(String filter) {
        List<Content> contentList = new ArrayList<>();

        if (filter.equals("top-rated")) {
            contentList = contentRepository.findByContentTypeOrderByRatingAverageDesc(ContentType.SERIES);
        }
        else if (filter.equals("now-playing")) {    // airing_today : 오늘 방영
            contentList = contentRepository.findByContentTypeAndReleaseDateBeforeOrderByReleaseDateDesc(
                    ContentType.SERIES, LocalDate.now());
        }
        else if (filter.equals("up-coming")) {      // on_the_air : 앞으로 7일 동안 방영되는
            contentList = contentRepository.findByContentTypeAndReleaseDateAfterOrderByReleaseDateAsc(
                    ContentType.SERIES, LocalDate.now());
        }
        else
            throw new BaseException(BaseResponseStatus.INVALID_QUERY_PARAMETER);

        List<Long> contentIds = contentList.stream()
                .map(Content::getId)
                .collect(Collectors.toList());

        List<Review> reviewList = reviewRepository.findTop10ByContentIdInOrderByReviewLikesDesc(contentIds);

        Map<Long, List<Review>> reviewsByContent = reviewList.stream()
                .collect(Collectors.groupingBy(review -> review.getContent().getId()));

        return contentList.stream()
                .map(content ->
                        SeriesTabResponse.of(content, reviewsByContent.getOrDefault(content.getId(), Collections.emptyList())))
                .toList();
    }

    public void updateLatestSeries() {
        CompletableFuture<List<TMDBSeriesDetailsRequest>> futureSeries = tmdbService.fetchLatestSeriesFromTMDB();
        List<TMDBSeriesDetailsRequest> detailedRequests = futureSeries.join();

        if (detailedRequests.isEmpty()) {
            System.out.println("가져온 TMDB 데이터 없음.");
            return;
        }

        List<String> titleList = detailedRequests.stream()
                .map(TMDBSeriesDetailsRequest::getOriginalName)
                .collect(Collectors.toList());

        List<LocalDate> dateList = detailedRequests.stream()
                .map(TMDBSeriesDetailsRequest::getFirstAirDate)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Set<String> existingTitles = contentRepository.findExistingTitles(titleList, dateList).stream()
                .map(result -> Objects.toString(result[0], "") + Objects.toString(result[1], ""))
                .collect(Collectors.toSet());

        List<Content> newSeriesList = detailedRequests.stream()
                .filter(request -> !existingTitles.contains(
                        request.getOriginalName() + Optional.ofNullable(request.getFirstAirDate()).map(Object::toString).orElse("")
                ))
                .map(TMDBSeriesDetailsRequest::toEntity)
                .collect(Collectors.toList());

        if (!newSeriesList.isEmpty()) {
            contentRepository.saveAll(newSeriesList);
            System.out.println("새로운 시리즈 저장 개수: " + newSeriesList.size());
        }
    }

    public String updateLatestMovies() {
        CompletableFuture<List<TMDBMovieDetailsRequest>> futureSeries = tmdbService.fetchLatestMoviesFromTMDB();
        List<TMDBMovieDetailsRequest> detailedRequests = futureSeries.join();
        if (detailedRequests.isEmpty()) {
            return "가져온 TMDB 데이터 없음.";
        }
        contentRepository.saveAll(detailedRequests.stream()
            .map(TMDBMovieDetailsRequest::toEntity)
            .toList());
        return "새로운 영화 저장 개수: " + detailedRequests.size();
    }

    // ------------------ 테스트용 메서드 ------------------------

    public Content addContent(Content content) {
        return contentRepository.save(content);
    }

}