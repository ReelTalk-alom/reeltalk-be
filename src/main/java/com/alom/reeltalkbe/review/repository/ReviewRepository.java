package com.alom.reeltalkbe.review.repository;

import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.user.dto.MyPageReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository <Review, Long> {
    Page<Review> findByContentId(Long contentId, Pageable pageable);

    List<Review> findByUserId(Long userId);


    List<Review> findTop10ByContentIdInOrderByReviewLikesDesc(List<Long> contentIds);

    List<Review> findTop10ByContentIdOrderByReviewLikesDesc(Long id);

    @Query("SELECT r FROM Review r " +
            "LEFT JOIN r.reviewLikes rl " +
            "GROUP BY r " +
            "ORDER BY SUM(CASE WHEN rl.likeType = 'LIKE' THEN 1 ELSE 0 END) DESC")
    List<Review> findTopReviews(Pageable pageable);

    // 마이페이지 관련 추가
    // mypage best review
    @Query("SELECT new com.alom.reeltalkbe.user.dto.MyPageReviewDto( " +
            "r.id,                 " + // 1 Long
            "r.title,             " + // 2 String
            "u.username,          " + // 3 String
            "u.id,                " + // 4 Long
            "r.overview,          " + // 5 String
            "r.videoPath,         " + // 6 String
            "r.createdAt,         " + // 7 LocalDateTime
            "r.duration,          " + // 8 Long
            "img.url,             " + // 9 String
            "CAST(COALESCE(SUM(CASE WHEN rl.likeType = 'LIKE' THEN 1 ELSE 0 END), 0) AS long), " + // 10 Long
            "CAST(COALESCE(SUM(CASE WHEN rl.likeType = 'HATE' THEN 1 ELSE 0 END), 0) AS long)) " + // 11 Long
            "FROM Review r " +
            "LEFT JOIN r.user u " +
            "LEFT JOIN r.image img " +
            "LEFT JOIN r.reviewLikes rl " +
            "WHERE r.user.id = :userId " +
            "GROUP BY r.id, r.title, u.username, u.id, r.overview, r.videoPath, r.createdAt, r.duration, img.url " +
            "ORDER BY SUM(CASE WHEN rl.likeType = 'LIKE' THEN 1 ELSE 0 END) DESC")
    List<MyPageReviewDto> findTopReviewsByUserId(@Param("userId") Long userId, Pageable pageable);

    // mypage recent review
    @Query("SELECT new com.alom.reeltalkbe.user.dto.MyPageReviewDto( " +
            "r.id,                 " + // 1 Long
            "r.title,             " + // 2 String
            "u.username,          " + // 3 String
            "u.id,                " + // 4 Long
            "r.overview,          " + // 5 String
            "r.videoPath,         " + // 6 String
            "r.createdAt,         " + // 7 LocalDateTime
            "r.duration,          " + // 8 Long
            "img.url,             " + // 9 String
            "CAST(COALESCE(SUM(CASE WHEN rl.likeType = 'LIKE' THEN 1 ELSE 0 END), 0) AS long), " + // 10 Long
            "CAST(COALESCE(SUM(CASE WHEN rl.likeType = 'HATE' THEN 1 ELSE 0 END), 0) AS long)) " + // 11 Long
            "FROM Review r " +
            "LEFT JOIN r.user u " +
            "LEFT JOIN r.image img " +
            "LEFT JOIN r.reviewLikes rl " +
            "WHERE r.user.id = :userId " +
            "GROUP BY r.id, r.title, u.username, u.id, r.overview, r.videoPath, r.createdAt, r.duration, img.url " +
            "ORDER BY r.createdAt DESC")
    List<MyPageReviewDto> findRecentReviewsByUserId(@Param("userId") Long userId, Pageable pageable);

    // 전체 리뷰 개수 조회
    @Query("SELECT COUNT(r) FROM Review r")
    long countAllReviews();
}

