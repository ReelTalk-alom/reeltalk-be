package com.alom.reeltalkbe.content.controller;

import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.content.domain.TMDB.Movie;
import com.alom.reeltalkbe.content.dto.TMDB.TMDBMovieDetailsRequest;
import com.alom.reeltalkbe.content.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieRepository movieRepository;
    /**
     * 클라이언트에서 JSON 형태의 영화 상세 정보를 보내면 DB에 저장합니다.
     *
     * 예)
     * POST /movies
     * Content-Type: application/json
     *
     * {
     *    "adult": false,
     *    "backdrop_path": "...",
     *    "belongs_to_collection": { ... },
     *    "genres": [ { "id": 16, "name": "Animation" }, ... ],
     *    "homepage": "https://movies.disney.com/moana-2",
     *    "id": 1241982,
     *    "imdb_id": "tt13622970",
     *    "origin_country": [ "US" ],
     *    "original_language": "en",
     *    "original_title": "Moana 2",
     *    "overview": "...",
     *    "popularity": 4841.214,
     *    "poster_path": "...",
     *    "release_date": "2024-11-21",
     *    "runtime": 99,
     *    "status": "Released",
     *    "tagline": "The ocean is calling them back.",
     *    "title": "Moana 2",
     *    "video": false,
     *    "vote_average": 7.22,
     *    "vote_count": 1519,
     *    "name_kor": "한글 제목",
     *    "popular": "인기순위",
     *    "latest": "최신개봉순",
     *    "review_videos": [ { ... }, ... ],
     *    "characters": [ { ... }, ... ]
     * }
     */
    @PostMapping
    public BaseResponse<Movie> createMovie(@RequestBody TMDBMovieDetailsRequest dto) {
        Movie movie = new Movie();
        movie.setAdult(dto.isAdult());
        movie.setBackdropPath(dto.getBackdropPath());
        movie.setBelongsToCollection(dto.getBelongsToCollection());
        movie.setGenres(dto.getGenres());
        movie.setHomepage(dto.getHomepage());
        movie.setId(dto.getId());
        movie.setImdbId(dto.getImdbId());
        movie.setOriginCountry(dto.getOriginCountry());
        movie.setOriginalLanguage(dto.getOriginalLanguage());
        movie.setOriginalTitle(dto.getOriginalTitle());
        movie.setOverview(dto.getOverview());
        movie.setPopularity(dto.getPopularity());
        movie.setPosterPath(dto.getPosterPath());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setRuntime(dto.getRuntime());
        movie.setStatus(dto.getStatus());
        movie.setTagline(dto.getTagline());
        movie.setTitle(dto.getTitle());
        movie.setVideo(dto.isVideo());
        movie.setVoteAverage(0);
        movie.setVoteCount(0);

        movie.setNameKor("기본 한글 제목");
        movie.setPopular("인기순위 몇위");
        movie.setLatest("최신개봉순");

        return new BaseResponse<>(movieRepository.save(movie));
    }

    @GetMapping
    public BaseResponse<List<Movie>> getMovies() {
        return new BaseResponse<>(movieRepository.findAll());
    }

    @GetMapping("/{movieId}")
    public BaseResponse<Movie> getMovieDetail(@PathVariable Long movieId) {
        Movie savedMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));

        return new BaseResponse<>(savedMovie);
    }
}
