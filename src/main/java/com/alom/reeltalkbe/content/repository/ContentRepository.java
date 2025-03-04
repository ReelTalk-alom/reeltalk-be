package com.alom.reeltalkbe.content.repository;


import com.alom.reeltalkbe.content.domain.Content;
import java.time.LocalDate;

import com.alom.reeltalkbe.content.domain.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findTop10ByOrderByReleaseDateDesc();
    List<Content> findTop10ByOrderByRatingAverageDesc();
    List<Content> findTop10ByReleaseDateBetweenOrderByPopularityDesc(LocalDate today, LocalDate monthLater);
    List<Content> findTop10ByReleaseDateAfterOrderByReleaseDateAsc(LocalDate today);


    List<Content> findTop10ByContentTypeOrderByReleaseDateAsc(ContentType contentType);

    List<Content> findByContentTypeOrderByRatingAverageDesc(ContentType contentType);

    // releaseDate가 오늘 이전인 컨텐츠(내림차순)
    List<Content> findByContentTypeAndReleaseDateBeforeOrderByReleaseDateDesc(ContentType contentType, LocalDate date);

    // releaseDate가 오늘 이후인 컨텐츠(오름차순)
    List<Content> findByContentTypeAndReleaseDateAfterOrderByReleaseDateAsc(ContentType contentType, LocalDate date);
}
