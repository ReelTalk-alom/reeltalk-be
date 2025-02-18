package com.alom.reeltalkbe.content.domain.TMDB;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BelongsToCollection {
    private Long id;
    private String name;
    private String posterPath;
    private String backdropPath;
}
