package com.alom.reeltalkbe.review.repository;

import com.alom.reeltalkbe.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}

