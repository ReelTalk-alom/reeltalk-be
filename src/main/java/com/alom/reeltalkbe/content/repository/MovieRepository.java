package com.alom.reeltalkbe.content.repository;

import com.alom.reeltalkbe.content.domain.TMDB.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

}
