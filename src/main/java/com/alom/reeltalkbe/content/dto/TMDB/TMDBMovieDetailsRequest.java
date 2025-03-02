package com.alom.reeltalkbe.content.dto.TMDB;

import com.alom.reeltalkbe.content.domain.Genre;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TMDBMovieDetailsRequest {

    private boolean adult;

    @JsonProperty("backdrop_path")
    private String backdropPath;
//
//    @JsonProperty("belongs_to_collection")
//    private BelongsToCollection belongsToCollection;

    private long budget;

    private List<Genre> genres;

    private String homepage;

    private long id;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("origin_country")
    private List<String> originCountry;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;

    private String overview;

    private double popularity;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("production_companies")
    private List<ProductionCompanyDto> productionCompanies;

    @JsonProperty("production_countries")
    private List<ProductionCountryDto> productionCountries;

    @JsonProperty("release_date")
    private LocalDate releaseDate;

    private long revenue;

    private int runtime;

    @JsonProperty("spoken_languages")
    private List<SpokenLanguageDto> spokenLanguages;

    private String status;

    private String tagline;

    private String title;

    private boolean video;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("vote_count")
    private int voteCount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductionCompanyDto {
        private long id;
        @JsonProperty("logo_path")
        private String logoPath;
        private String name;
        @JsonProperty("origin_country")
        private String originCountry;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductionCountryDto {
        @JsonProperty("iso_3166_1")
        private String iso31661;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpokenLanguageDto {
        @JsonProperty("english_name")
        private String englishName;
        @JsonProperty("iso_639_1")
        private String iso6391;
        private String name;
    }
}
