package com.alom.reeltalkbe.content.dto.TMDB;

import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.domain.Genre;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TMDBSeriesDetailsRequest {

    private boolean adult;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("created_by")
    private List<CreatedByDto> createdBy;

    @JsonProperty("episode_run_time")
    private List<Integer> episodeRunTime;

    @JsonProperty("first_air_date")
    private LocalDate firstAirDate;

    private List<Genre> genres;

    private String homepage;

    private long id;

    @JsonProperty("in_production")
    private boolean inProduction;

    private List<String> languages;

    @JsonProperty("last_air_date")
    private String lastAirDate;

    @JsonProperty("last_episode_to_air")
    private EpisodeDto lastEpisodeToAir;

    @JsonProperty("name")
    private String title;

    @JsonProperty("original_name")
    private String originalTitle;

    @JsonProperty("next_episode_to_air")
    private EpisodeDto nextEpisodeToAir;

    private List<NetworkDto> networks;

    @JsonProperty("number_of_episodes")
    private int numberOfEpisodes;

    @JsonProperty("number_of_seasons")
    private int numberOfSeasons;

    @JsonProperty("origin_country")
    private List<String> originCountry;

    @JsonProperty("original_language")
    private String originalLanguage;

    private String overview;

    private double popularity;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("production_companies")
    private List<ProductionCompanyDto> productionCompanies;

    @JsonProperty("production_countries")
    private List<ProductionCountryDto> productionCountries;

    private List<SeasonDto> seasons;

    @JsonProperty("spoken_languages")
    private List<SpokenLanguageDto> spokenLanguages;

    private String status;

    private String tagline;

    private String type;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("vote_count")
    private int voteCount;

    // ===================== 내부 DTO 클래스 =====================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatedByDto {
        private long id;
        @JsonProperty("credit_id")
        private String creditId;
        private String name;
        private int gender;
        @JsonProperty("profile_path")
        private String profilePath;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EpisodeDto {
        private long id;
        private String name;
        private String overview;
        @JsonProperty("vote_average")
        private double voteAverage;
        @JsonProperty("vote_count")
        private int voteCount;
        @JsonProperty("air_date")
        private String airDate;
        @JsonProperty("episode_number")
        private int episodeNumber;
        @JsonProperty("production_code")
        private String productionCode;
        private int runtime;
        @JsonProperty("season_number")
        private int seasonNumber;
        @JsonProperty("show_id")
        private long showId;
        @JsonProperty("still_path")
        private String stillPath;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NetworkDto {
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
    public static class SeasonDto {
        @JsonProperty("air_date")
        private String airDate;
        @JsonProperty("episode_count")
        private int episodeCount;
        private long id;
        private String name;
        private String overview;
        @JsonProperty("poster_path")
        private String posterPath;
        @JsonProperty("season_number")
        private int seasonNumber;
        @JsonProperty("vote_average")
        private double voteAverage;
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

    public Content toEntity() {
        return new Content(this);
    }

}
