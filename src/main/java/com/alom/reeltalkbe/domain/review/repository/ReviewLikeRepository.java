package com.alom.reeltalkbe.domain.review.repository;

import com.alom.reeltalkbe.domain.review.domain.Review;
import com.alom.reeltalkbe.domain.review.domain.reviewLike.ReviewLike;
import com.alom.reeltalkbe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository  extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByUserAndReview(User user, Review review);
}
