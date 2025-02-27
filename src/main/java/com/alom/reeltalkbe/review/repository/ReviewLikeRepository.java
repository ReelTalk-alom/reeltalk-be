package com.alom.reeltalkbe.review.repository;

import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.domain.reviewLike.ReviewLike;
import com.alom.reeltalkbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository  extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByUserAndReview(User user, Review review);
}
