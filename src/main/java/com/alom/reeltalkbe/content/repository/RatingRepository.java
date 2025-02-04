package com.alom.reeltalkbe.content.repository;

import com.alom.reeltalkbe.content.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findRatingByContentAndUser(Long content_id, Long user_id);
}
