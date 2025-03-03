package com.alom.reeltalkbe.content.dto;

import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.domain.Genre;
import com.alom.reeltalkbe.content.domain.GenreListConverter;
import com.alom.reeltalkbe.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Convert;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SeriesTabResponse {

    private Long id;
    private boolean adult;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @Convert(converter = GenreListConverter.class)
    @JsonProperty("genres")
    private List<Genre> genres;

    private String overview;
    private double popularity;
    private double rating;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("first_air_date")
    private LocalDate firstAirDate;

    private String title;

    @JsonProperty("number_of_seasons")
    private int numberOfSeasons;

    @JsonProperty("number_of_episodes")
    private int numberOfEpisodes;

    private List<Review> reviews;

    @Builder
    private SeriesTabResponse(Content content, List<Review> reviews) {
        this.id = content.getId();
        this.adult = content.isAdult();
        this.backdropPath = content.getBackdropPath();
        this.genres = content.getGenres();
        this.overview = content.getOverview();
        this.popularity = content.getPopularity();
        this.rating = content.getRatingAverage();
        this.posterPath = content.getPosterPath();
        this.firstAirDate = content.getReleaseDate();  // 시리즈에서는 releaseDate가 firstAirDate로 매핑됨
        this.title = content.getEnTitle();
        this.numberOfSeasons = content.getNumberOfSeasons();
        this.numberOfEpisodes = content.getNumberOfEpisodes();
        this.reviews = reviews;
    }

    public static SeriesTabResponse of(Content content, List<Review> reviews) {
        return SeriesTabResponse.builder()
                .content(content)
                .reviews(reviews)
                .build();
    }
}
