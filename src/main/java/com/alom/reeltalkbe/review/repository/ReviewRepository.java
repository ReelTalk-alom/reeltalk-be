package com.alom.reeltalkbe.review.repository;

import com.alom.reeltalkbe.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository <Review, Long> {
    Page<Review> findByContentId(Long contentId, Pageable pageable);

    List<Review> findByUserId(Long userId);

    Optional<Object> findTopByUserIdOrderByRatingSumDesc(Long userId);

    List<Review> findTop10ByContentIdIn(List<Long> contentId);
    List<Review> findAllByContentId(Long contentId);
}

