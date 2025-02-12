package com.alom.reeltalkbe.content.service;

import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.domain.Rating;
import com.alom.reeltalkbe.content.dto.RatingDto;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.content.repository.RatingRepository;
import com.alom.reeltalkbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final RatingRepository ratingRepository;

    // 포스터 이미지와 링크만 가져오기?
    public List<Content> findPopularContentImages() {
        //
        return contentRepository.findAll();
    }

    public Content findContentById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));
    }

    public Content addRating(Long contentId, String username, RatingDto ratingDto) {
        // 이미 평가한 컨텐츠라면 예외 처리
        // todo: user pk 이름 id로 바꾸기? -> getUserId() 메서드도 getId() 로 바뀜
        if (ratingRepository.findRatingByContentIdAndUserUsername(contentId, username).isPresent()) {
            throw new BaseException(BaseResponseStatus.EXIST_RATING);
        }
        // content 조회
        Content content = findContentById(contentId);

        // user 조회 후 rating 빌더 패턴 사용
        Rating rating = Rating
                .builder()
                .user(userRepository.findByUsername(username)
                        .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER)))
                .content(content)
                .ratingValue(ratingDto.getRating())
                .build();

        // 컨텐츠 평균 평점 계산 후 rating 객체 DB에 저장
        content.updateRating(rating);
        ratingRepository.save(rating);
        return content;
    }

    public Content deleteRating(Long contentId, String username) {
        // content, rating 조회
        Content content = findContentById(contentId);
        Rating rating = ratingRepository.findRatingByContentIdAndUserUsername(contentId, username)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.RATING_NOT_FOUND));

        content.deleteRating(rating);
        ratingRepository.delete(rating);
        return content;
    }



    // ------------------ 테스트용 메서드 ------------------------
    public Content addContent(Content content) {
        return contentRepository.save(content);
    }
}