package com.alom.reeltalkbe.image.repository;


import com.alom.reeltalkbe.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByUrl(String url);

}
