package com.alom.reeltalkbe.content.domain;


import com.alom.reeltalkbe.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "content")
@Getter
public class Content extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ratingCount;
    private int ratingSum;
    private double ratingAverage;

    private String title;
    private String overview;
    private String genre;
    private String nation;
    private String director;
    private String actor;
    private Date released_at;

    // todo : 이미지 두장 추가 (세로 포스터, 가로 포스터)
//    @Column(name = "backdrop_path")
//    private Image poster;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    // test 용
    public Content() {
        ratingSum = 0;
        ratingCount = 0;
        ratingAverage = 0;
        // todo : 한글 / 영문 실시간 검색 기능 (한자 한자 칠때마다 비슷한 영화제목 반환)
        title = "Avengers 4";
        overview = "";
        genre = "horror, asdasd";
        nation = "asdasdsa";
        director = "john ber";
        actor = "asdasf, asdsdf, vsdaf";
        released_at = new Date();
        contentType = ContentType.MOVIE;
    }

    public void updateRating(Rating rating) {
        ratingCount++;
        ratingSum += rating.getRatingValue();
        ratingAverage = (double) ratingSum / ratingCount;
    }

    public void deleteRating(Rating rating) {
        ratingCount--;
        ratingSum -= rating.getRatingValue();
        //테스트시 count = 0일때는 무한대값이 되어버려, 따로 처리
        if (ratingCount == 0)
            ratingAverage = 0;
        else
            ratingAverage = (double) ratingSum / ratingCount;
    }
}
enum ContentType{
    MOVIE, SERIES
}
