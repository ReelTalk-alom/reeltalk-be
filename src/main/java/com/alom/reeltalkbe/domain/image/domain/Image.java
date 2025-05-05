package com.alom.reeltalkbe.domain.image.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    // URL 업데이트 메서드
    public void updateIfPresent(String url) {
        if (url != null && !url.isEmpty()) this.url = url;
    }


}
