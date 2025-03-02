package com.alom.reeltalkbe.content.service;

import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.dto.ContentDetailsResponse;
import com.alom.reeltalkbe.content.dto.MovieTabResponse;
import com.alom.reeltalkbe.content.dto.ReviewResponse;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.repository.ReviewRepository;
import com.alom.reeltalkbe.talk.domain.TalkMessage;
import com.alom.reeltalkbe.talk.repository.TalkMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        // todo : jpa orderby 좋아요수? 같은거 추가
        List<ReviewResponse> reviews = reviewRepository.findAllByContentId(content.getId())
            .stream()
            .map(ReviewResponse::of)
            .toList();
        // todo : talkMessages 시간순으로 나오는지 보기
        List<TalkMessage> talkMessages = talkMessageRepository.findAllByContentId(content.getId());
        return ContentDetailsResponse.of(content, reviews, talkMessages);
    }

    public List<MovieTabResponse> findMoviesAndReviewsSortBy(String sort) {
        List<Content> contentList = new ArrayList<>();

        if(sort.equals("releaseDate")) {    // todo : 분류명 추가 가능
            contentList = contentRepository.findTop10ByOrderByReleaseDateAsc();
        }

        List<Long> contentIds = contentList.stream()
                .map(Content::getId)
                .collect(Collectors.toList());

        // todo : jpa orderby 좋아요수? 같은거 추가
        List<Review> reviewList = reviewRepository.findTop10ByContentIdIn(contentIds);

        Map<Long, List<Review>> reviewsByContent = reviewList.stream()
                .collect(Collectors.groupingBy(review -> review.getContent().getId()));

        return contentList.stream()
                .map(content -> MovieTabResponse.of(content, reviewsByContent
                    .getOrDefault(content.getId(), Collections.emptyList())))
                .toList();
    }

    public String findSeriesAndReviewSortBy() {
        return "Hmm....";
    }

    // ------------------ 테스트용 메서드 ------------------------
    public Content addContent(Content content) {
        return contentRepository.save(content);
    }

}