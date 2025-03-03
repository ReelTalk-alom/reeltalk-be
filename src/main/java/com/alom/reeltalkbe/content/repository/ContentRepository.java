package com.alom.reeltalkbe.content.repository;


import com.alom.reeltalkbe.content.domain.Content;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findTop10ByOrderByReleaseDateDesc();
    List<Content> findTop10ByOrderByRatingAverageDesc();
    List<Content> findTop10ByReleaseDateBetweenOrderByPopularityDesc(LocalDate today, LocalDate monthLater);
    List<Content> findTop10ByReleaseDateAfterOrderByReleaseDateAsc(LocalDate today);
}
