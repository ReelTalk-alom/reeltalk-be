package com.alom.reeltalkbe.content.domain;


import com.alom.reeltalkbe.common.BaseEntity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "content")
public class Content extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ratingCount;
    private int ratingSum;

    private String title;
    private String plot;
    private String genre;
    private String nation;
    private String director;
    private String actor;
    private Date released_at;

    public void updateRating(Rating rating) {
        ratingCount++;
        ratingSum += rating.getRatingValue();
    }

    public void deleteRating(Rating rating) {
        ratingCount--;
        ratingSum -= rating.getRatingValue();
    }
}
