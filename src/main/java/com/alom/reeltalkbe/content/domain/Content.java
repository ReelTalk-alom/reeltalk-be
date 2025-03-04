package com.alom.reeltalkbe.content.domain;


import com.alom.reeltalkbe.common.BaseEntity;
import com.alom.reeltalkbe.content.dto.TMDB.TMDBMovieDetailsRequest;
import com.alom.reeltalkbe.content.dto.TMDB.TMDBSeriesDetailsRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/* ㅁㅁ 테스터용 추가
@Builder
@AllArgsConstructor
*/

@Entity
@Table(name = "content")
@Getter
public class Content extends BaseEntity {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)  //ㅁㅁ 테스터용 자동 증가 추가
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
    private LocalDate releaseDate;
    //private String releaseDate;
    private int runtime;
    private String tagline;

    private ContentType contentType;

    // series에만 필요
    @Column(name = "number_of_seasons")
    private Integer numberOfSeasons;
    @Column(name = "number_of_episodes")
    private Integer numberOfEpisodes;

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

    // test용
    public Content(TMDBSeriesDetailsRequest request) {
        this.id = request.getId();
        this.enTitle = request.getOriginalName();
        this.korTitle = request.getName();
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
        this.releaseDate = request.getFirstAirDate(); // 시리즈의 경우 첫 방영일을 출시일로 사용
        this.runtime = (request.getEpisodeRunTime() != null && !request.getEpisodeRunTime().isEmpty())
                ? request.getEpisodeRunTime().get(0)  // 첫 번째 에피소드의 런타임 사용
                : 0;
        this.tagline = request.getTagline();
        this.contentType = ContentType.SERIES;
        this.numberOfSeasons = request.getNumberOfSeasons();
        this.numberOfEpisodes = request.getNumberOfEpisodes();
    }

    /* ㅁㅁ 테스터용 추가
    @Builder
    public Content(String enTitle, String korTitle, String country, ContentType contentType) {
        this.enTitle = enTitle;
        this.korTitle = korTitle;
        this.country = country;
        this.contentType = contentType;
    }
    */

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
