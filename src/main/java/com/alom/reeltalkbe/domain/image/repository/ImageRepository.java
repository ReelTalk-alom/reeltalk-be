package com.alom.reeltalkbe.domain.image.repository;


import com.alom.reeltalkbe.domain.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByUrl(String url);

    void deleteByUrl(String url);

}
