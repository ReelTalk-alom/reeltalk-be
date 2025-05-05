package com.alom.reeltalkbe.domain.content.repository;

import com.alom.reeltalkbe.domain.content.domain.ContentRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<ContentRating, Long> {
    Optional<ContentRating> findRatingByContentIdAndUserId(Long contentId, Long userId);
}
