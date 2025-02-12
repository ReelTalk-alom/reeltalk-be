package com.alom.reeltalkbe.review.repository;

import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.domain.ReviewRating;
import com.alom.reeltalkbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Long> {
    Optional<ReviewRating> findByUserAndReview(User user, Review review);
}
