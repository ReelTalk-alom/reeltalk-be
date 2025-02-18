package com.alom.reeltalkbe.content.domain.TMDB;

import com.alom.reeltalkbe.review.domain.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "movie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    // TMDB 영화 ID를 기본 키로 사용
    @Id
    private Long id;

    private boolean adult;

    private String backdropPath;

    // 컬렉션 정보 (Embeddable 객체)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "collection_id")),
            @AttributeOverride(name = "backdropPath", column = @Column(name = "collection_backdrop_path")),
            @AttributeOverride(name = "posterPath", column = @Column(name = "collection_poster_path"))
    })
    private BelongsToCollection belongsToCollection;

    // 장르 정보 (여러 개의 장르)
    @ElementCollection
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    private List<Genre> genres;

    private String homepage;

    private String imdbId;

    // 원산지 국가 (문자열 리스트)
    @ElementCollection
    @CollectionTable(name = "movie_origin_country", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "origin_country")
    private List<String> originCountry;

    private String originalLanguage;
    private String originalTitle;
    @Lob
    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;
    private double popularity;
    private String posterPath;
    private String releaseDate;
    private int runtime;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;

    // 추가 필드
    private String nameKor; // 한글 제목
    private String popular; // 인기순위
    private String latest;  // 최신 개봉순

//    // 리뷰 영상 (하나의 영화에 여러 리뷰 영상)
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "movie_id")
//    private List<Review> reviewVideos;

//    // 등장인물 (하나의 영화에 여러 등장인물)
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "movie_id")
//    private String characters;



}

