package com.alom.reeltalkbe.content.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;


//@Entity
@Getter
public class Character {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id")
    private Long contentId;

    private String name;

    @JsonProperty("name_real")
    private String nameReal;
    private String image;

}
