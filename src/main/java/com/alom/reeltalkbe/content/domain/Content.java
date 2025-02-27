package com.alom.reeltalkbe.content.domain;


import com.alom.reeltalkbe.common.BaseEntity;
import com.alom.reeltalkbe.content.dto.TMDB.TMDBMovieDetailsRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "content")
@Getter
public class Content extends BaseEntity {
    @Id
    private Long id;

    @JsonProperty("en_title")
    private String enTitle;
    @JsonProperty("kor_title")
    private String korTitle;
    private boolean adult;
    @JsonProperty("backdrop_path")
    private String backdropPath;

    private String country;

    @Column(columnDefinition = "TEXT")
    private String overview;
    private double popularity;

    private int ratingCount;
    private int ratingSum;
    private double ratingAverage;

    @Convert(converter = GenreListConverter.class)
    @Column(name = "genres", columnDefinition = "TEXT")
    private List<Genre> genres;

    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("release_date")
    private String releaseDate;
    private int runtime;
    private String tagline;

    private ContentType contentType;

    // test 용
    public Content(TMDBMovieDetailsRequest request) {
        this.id = request.getId();
        this.enTitle = request.getOriginalTitle();
        this.korTitle = request.getTitle();
        this.adult = request.isAdult();
        this.backdropPath = request.getBackdropPath();
        this.country = (request.getOriginCountry() != null && !request.getOriginCountry().isEmpty())
                ? String.join(",", request.getOriginCountry())
                : "";
        this.overview = request.getOverview();
        this.popularity = request.getPopularity();
        this.ratingCount = 0;
        this.ratingSum = 0;
        this.ratingAverage = 0.0;
        this.genres = request.getGenres();
        this.posterPath = request.getPosterPath();
        this.releaseDate = request.getReleaseDate();
        this.runtime = request.getRuntime();
        this.tagline = request.getTagline();
        this.contentType = ContentType.MOVIE;
    }

    public Content() {}

    public void updateRating(ContentRating rating) {
        ratingCount++;
        ratingSum += rating.getRatingValue();
        ratingAverage = (double) ratingSum / ratingCount;
    }

    public void deleteRating(ContentRating rating) {
        ratingCount--;
        ratingSum -= rating.getRatingValue();
        //테스트시 count = 0일때는 무한대값이 되어버려, 따로 처리
        if (ratingCount == 0)
            ratingAverage = 0;
        else
            ratingAverage = (double) ratingSum / ratingCount;
    }
}
