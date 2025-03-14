package com.alom.reeltalkbe.content.dto.TMDB;

import java.util.List;
import lombok.Getter;

@Getter
public class TMDBMovieResponse {
    private List<TMDBMovieDetailsRequest> results;

}