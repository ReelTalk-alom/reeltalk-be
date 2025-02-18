package com.alom.reeltalkbe.content.dto.TMDB;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class MovieResponse {
    // TMDB 기본 정보
    private boolean adult;

    // JSON snake case - Java camel case 매핑
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("belongs_to_collection")
    private BelongsToCollection belongsToCollection;

    private List<Genre> genres;
    private String homepage;
    private Long id;

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

    @JsonProperty("release_date")
    private String releaseDate;

    private int runtime;
    private String status;
    private String tagline;
    private String title;
    private boolean video;

    @JsonProperty("vote_average")
    private double voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;

    // --------------- 한글 필드 ----------------------
    @JsonProperty("name_kor")
    private String nameKor;

    // 인기순위 (필요에 따라 숫자나 문자열로 변경)
    private String popular;

    // 최신 개봉순 (필요에 따라 날짜나 문자열로 변경)
    private String latest;

    @JsonProperty("review_videos")
    private List<ReviewVideo> reviewVideos;

    private List<MovieCharacter> characters;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BelongsToCollection {
        private Long id;
        private String name;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("backdrop_path")
        private String backdropPath;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Genre {
        private int id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewVideo {
        private Long id;
        private String name;
        private String author;

        @JsonProperty("author_id")
        private Long authorId;

        private String overview;

        @JsonProperty("video_path")
        private String videoPath;

        @JsonProperty("published_at")
        private String publishedAt;

        // 영상 길이 (예를 들어 초 단위 혹은 "HH:mm:ss" 형식)
        private String duration;

        private String thumbnail;

        @JsonProperty("like_count")
        private int likeCount;

        // 리뷰 별점 정보 – 추후 결정
        private String popularity;

        private List<Comment> comments;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comment {
        @JsonProperty("comment_id")
        private Long commentId;
        private String user;

        @JsonProperty("user_img")
        private String userImg;

        private String title;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("like_count")
        private int likeCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieCharacter {
        @JsonProperty("character_id")
        private Long characterId;

        // 극중 이름
        private String name;

        // 배우 이름
        @JsonProperty("name_real")
        private String nameReal;

        // 배우 사진 주소
        private String image;
    }

}