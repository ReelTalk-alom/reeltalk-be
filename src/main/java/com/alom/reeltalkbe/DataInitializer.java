package com.alom.reeltalkbe;

import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.domain.Genre;
import com.alom.reeltalkbe.content.dto.TMDB.TMDBMovieDetailsRequest;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.image.domain.Image;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.repository.ReviewRepository;
import com.alom.reeltalkbe.review.service.YouTubeService;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final YouTubeService youTubeService; // YouTube API 서비스 추가

    @Bean
    @Transactional
    public CommandLineRunner initDatabase(
            UserRepository userRepository,
            ContentRepository contentRepository,
            ReviewRepository reviewRepository,
            BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            // 유저 데이터 초기화
            List<User> users = userRepository.saveAll(getUsers(passwordEncoder));
            log.info("유저 데이터가 초기화되었습니다.");

//            // 컨텐츠 데이터 초기화
//            List<Content> contents = contentRepository.saveAll(getContents());
//            log.info("콘텐츠 데이터 (쇼생크 탈출, 포레스트 검프, 대부) 저장 완료!");

            // 리뷰 데이터 초기화 (각 컨텐츠에 대해 리뷰 삽입)
            Map<Long, List<String>> contentReviews = getContentReviews();
            for (Long contentId : contentReviews.keySet()) {
                saveReviewsForContent(contentId, users, contentReviews.get(contentId), contentRepository, reviewRepository, youTubeService);
            }
        };
    }

    // 유저 데이터 생성
    private List<User> getUsers(BCryptPasswordEncoder passwordEncoder) {
        return Arrays.asList(
                User.builder().email("user1@gmail.com").password(passwordEncoder.encode("password123")).username("김진호").role("ROLE_USER").build(),
                User.builder().email("user2@gmail.com").password(passwordEncoder.encode("password123")).username("로이킴").role("ROLE_USER").build(),
                User.builder().email("user3@gmail.com").password(passwordEncoder.encode("password123")).username("이문세").role("ROLE_USER").build(),
                User.builder().email("user4@gmail.com").password(passwordEncoder.encode("password123")).username("나얼").role("ROLE_USER").build(),
                User.builder().email("user5@gmail.com").password(passwordEncoder.encode("password123")).username("김범수").role("ROLE_USER").build(),
                User.builder().email("user6@gmail.com").password(passwordEncoder.encode("password123")).username("성시경").role("ROLE_USER").build(),
                User.builder().email("user7@gmail.com").password(passwordEncoder.encode("password123")).username("버즈").role("ROLE_USER").build(),
                User.builder().email("user8@gmail.com").password(passwordEncoder.encode("password123")).username("허각").role("ROLE_USER").build(),
                User.builder().email("user9@gmail.com").password(passwordEncoder.encode("password123")).username("박효신").role("ROLE_USER").build(),
                User.builder().email("user10@gmail.com").password(passwordEncoder.encode("password123")).username("송이한").role("ROLE_USER").build()
        );
    }

    // 컨텐츠 데이터 생성
    private List<Content> getContents() {
        List<TMDBMovieDetailsRequest> tmdbMovies = List.of(
                new TMDBMovieDetailsRequest(false, "/zfbjgQE1uSd9wiPTX4VzsLi0rGG.jpg",
                        0L, List.of(new Genre(18, "드라마"), new Genre(80, "범죄")), "", 278L,
                        "", List.of("US"), "en", "The Shawshank Redemption",
                        "촉망받는 은행 간부 앤디 듀프레인은 아내와 그녀의 정부를 살해했다는 누명을 쓴다...",
                        6.81, "/oAt6OtpwYCdJI76AVtVKW1eorYx.jpg",
                        List.of(), List.of(), LocalDate.of(1994, 9, 23), 0L, 0,
                        List.of(), "Released", "", "쇼생크 탈출", false, 0.0, 0),

                new TMDBMovieDetailsRequest(false, "/mzfx54nfDPTUXZOG48u4LaEheDy.jpg",
                        0L, List.of(new Genre(35, "코미디"), new Genre(18, "드라마"), new Genre(10749, "로맨스")), "", 13L,
                        "", List.of("US"), "en", "Forrest Gump",
                        "불편한 다리, 남들보다 조금 떨어지는 지능을 가진 포레스트 검프는...",
                        4.991, "/iraQz6gdAe8JL45QcBifM1UhQ38.jpg",
                        List.of(), List.of(), LocalDate.of(1994, 6, 23), 0L, 0,
                        List.of(), "Released", "", "포레스트 검프", false, 0.0, 0),
                new TMDBMovieDetailsRequest(
                        false, "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
                        0L, List.of(new Genre(18, "드라마"), new Genre(80, "범죄")), "", 238L,
                        "", List.of("US"), "en", "The Godfather",
                        "시실리에서 이민온 뒤, 정치권까지 영향력을 미치는 거물로 자리잡은 돈 꼴레오네는...",
                        5.445, "/I1fkNd5CeJGv56mhrTDoOeMc2r.jpg",
                        List.of(), List.of(),
                        LocalDate.of(1972, 3, 14), 0L, 0,
                        List.of(), "Released", "", "대부", false, 0.0, 0
                )
        );

        return tmdbMovies.stream().map(Content::new).toList();
    }

    // 컨텐츠 ID별 유튜브 리뷰 링크 매핑
    private Map<Long, List<String>> getContentReviews() {
        return Map.of(
                278L, List.of(
                        "https://www.youtube.com/watch?v=QnXZAYMRSv0",
                        "https://www.youtube.com/watch?v=NM7MSoOKRNc",
                        "https://www.youtube.com/watch?v=hshWIqAyl9E",
                        "https://www.youtube.com/watch?v=YGwhQLmUWoo",
                        "https://www.youtube.com/watch?v=aMytlWuXdk0",
                        "https://www.youtube.com/watch?v=Co7Y6yK0YUE",
                        "https://www.youtube.com/watch?v=8ecTKUtmsiI",
                        "https://www.youtube.com/watch?v=WmCjOfCGFrE",
                        "https://www.youtube.com/watch?v=bAWvR2ulCJ4",
                        "https://www.youtube.com/watch?v=Y5VGXB6x0Wk"),
                13L, List.of(
                        "https://www.youtube.com/watch?v=M4iV1CswVH0",
                        "https://www.youtube.com/watch?v=HdpaeJiHYoU",
                        "https://www.youtube.com/watch?v=mtcgwsGzlwk",
                        "https://www.youtube.com/watch?v=5BTu0Th-jhA",
                        "https://www.youtube.com/watch?v=xC3loNtNuzE",
                        "https://www.youtube.com/watch?v=4ZAYNHTe_o8",
                        "https://www.youtube.com/watch?v=o0JP-dBjk2Q",
                        "https://www.youtube.com/watch?v=i0eOJF17_Ck",
                        "https://www.youtube.com/watch?v=lSk9h2hjtIo",
                        "https://www.youtube.com/watch?v=nGUh4NDtIac"),
                238L,List.of(
                        "https://www.youtube.com/watch?v=l9Ehg-zjLjo",
                        "https://www.youtube.com/watch?v=5DKsyV6fpjw",
                        "https://www.youtube.com/watch?v=wbCxu3n8j4g",
                        "https://www.youtube.com/watch?v=u-Eh8TP6ez0",
                        "https://www.youtube.com/watch?v=ko2WsVgboOc",
                        "https://www.youtube.com/watch?v=9NsvgidIODg",
                        "https://www.youtube.com/watch?v=UCp_DTV0_9M",
                        "https://www.youtube.com/watch?v=i_QzRoxull0",
                        "https://www.youtube.com/watch?v=BT7RkyqGUZs",
                        "https://www.youtube.com/watch?v=bm9qAwbJlHg")
        );
    }

    // 특정 컨텐츠에 대한 리뷰 저장 (유저와 유튜브 영상 1:1 매칭)
    private void saveReviewsForContent(Long contentId, List<User> users, List<String> videoUrls,
                                       ContentRepository contentRepository, ReviewRepository reviewRepository,
                                       YouTubeService youTubeService) {

        Content content = contentRepository.findById(contentId).orElse(null);

        if (content == null) {
            log.warn("해당 컨텐츠({})를 찾을 수 없습니다.", contentId);
            return;
        }


        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < videoUrls.size(); i++) {
            User user = users.get(i);
            String videoUrl = videoUrls.get(i);

            String title = youTubeService.getVideoTitle(videoUrl);
            String thumbnailUrl = youTubeService.getThumbnailUrl(videoUrl);

            Image image = Image.builder().url(thumbnailUrl).build();
            Review review = Review.builder()
                    .content(content)
                    .user(user)
                    .image(image)
                    .overview("이 영상은 정말 대단합니다! 리뷰어: " + user.getUsername())
                    .videoPath(videoUrl)
                    .title(title)
                    .build();

            reviews.add(review);
        }

        reviewRepository.saveAll(reviews);
        log.info("컨텐츠({})에 {}개의 리뷰가 저장되었습니다.", content.getKorTitle(), reviews.size());
    }
}
