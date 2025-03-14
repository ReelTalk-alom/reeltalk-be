package com.alom.reeltalkbe.content.dto;

import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.domain.ContentType;
import com.alom.reeltalkbe.content.domain.Genre;
import com.alom.reeltalkbe.content.domain.GenreListConverter;
import com.alom.reeltalkbe.review.domain.Review;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Convert;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MovieTabResponse {

    private Long id;
    private boolean adult;

    private String backdropPath;

    @Convert(converter = GenreListConverter.class)
    @JsonProperty("genres")
    private List<Genre> genres; // MovieDetails랑 같게 세팅, id만 따로 안해도 되면 이게 편함

    private String overview;
    private double popularity;
    private double rating;

    private String posterPath;

    private LocalDate releaseDate;

    private String originalTitle;
    private String title;

    private List<ReviewResponse> reviews;

    private ContentType contentType;

    @Builder
    private MovieTabResponse(Content content, List<ReviewResponse> reviews) {
        id = content.getId();
        adult = content.isAdult();
        backdropPath = content.getBackdropPath();
        genres = content.getGenres();
        overview = content.getOverview();
        popularity = content.getPopularity();
        rating = content.getRatingAverage();
        posterPath = content.getPosterPath();
        releaseDate = content.getReleaseDate();
        originalTitle = content.getEnTitle();
        title = content.getKorTitle();
        contentType = content.getContentType();
        this.reviews = reviews;
    }

    public static MovieTabResponse of(Content content, List<Review> reviews) {
        List<ReviewResponse> reviewResponses = reviews.stream()
            .map(ReviewResponse::of)
            .toList();

        return MovieTabResponse.builder()
                .content(content)
                .reviews(reviewResponses)
                .build();
    }
}
