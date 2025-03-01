package com.alom.reeltalkbe.content.repository;


import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.domain.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findTop10ByContentTypeOrderByReleaseDateAsc(ContentType contentType);

}
