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
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.repository.ReviewRepository;
import com.alom.reeltalkbe.talk.domain.TalkMessage;
import com.alom.reeltalkbe.talk.repository.TalkMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final ReviewRepository reviewRepository;
    private final TalkMessageRepository talkMessageRepository;
    //private final CharacterRepository characterRepository;
    // tmdb 이슈로 캐릭터는 고민해야함

    // 포스터 이미지와 링크만 가져오기?
    public List<Content> findPopularContents() {
        //
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
        else if (filter.equals("now-playing")) {
            contentList = contentRepository.findByContentTypeAndReleaseDateBeforeOrderByReleaseDateDesc(
                    ContentType.SERIES, LocalDate.now());
        }
        else if (filter.equals("up-coming")) {
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

    // ------------------ 테스트용 메서드 ------------------------
    public Content addContent(Content content) {
        return contentRepository.save(content);
    }

}