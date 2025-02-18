package com.alom.reeltalkbe.review.repository;

import com.alom.reeltalkbe.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository <Review, Long> {
    Page<Review> findByContentId(Long contentId, Pageable pageable);

}

