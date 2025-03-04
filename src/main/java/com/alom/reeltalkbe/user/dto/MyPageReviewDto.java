package com.alom.reeltalkbe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPageReviewDto {
    private Long id;                // 1
    private String title;           // 2
    private String username;        // 3
    private Long user_id;           // 4
    private String overview;        // 5
    private String video_path;      // 6
    private LocalDateTime published_at; // 7
    private Long duration;          // 8
    private String thumbnail;       // 9
    private Long like_count;        // 10
    private Long hate_count;        // 11
}
