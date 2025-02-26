package com.alom.reeltalkbe.content.domain;

import lombok.*;

import jakarta.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private Integer id;
    private String name;
}