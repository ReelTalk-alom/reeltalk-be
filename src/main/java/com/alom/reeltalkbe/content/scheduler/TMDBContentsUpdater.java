package com.alom.reeltalkbe.content.scheduler;

import com.alom.reeltalkbe.content.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TMDBContentsUpdater {

    private final ContentService contentService;

    @Scheduled(cron = "0 0 2 * * ?") // 매일 새벽 2시에 실행
    public void updateSeriesContent() {
        contentService.updateLatestSeries();
        contentService.updateLatestMovies();
    }
}
