package com.alom.reeltalkbe.content.domain.TMDB;

import lombok.*;

import jakarta.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private int id;
    private String name;
}
