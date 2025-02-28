package com.alom.reeltalkbe.review.service;

import com.alom.reeltalkbe.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private final RestTemplate restTemplate;

    public String getVideoTitle(String videoUrl) {
        String videoId = extractYouTubeVideoId(videoUrl);
//        if (videoId == null) {
//            throw new BaseException(BaseResponseStatus.INVALID_VIDEO_URL);
//        }

        String apiUrl = "https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=" + videoId + "&format=json";
        String response = restTemplate.getForObject(apiUrl, String.class);
        JSONObject json = new JSONObject(response);

        return json.getString("title");
    }

    public String getThumbnailUrl(String videoUrl) {
        String videoId = extractYouTubeVideoId(videoUrl);
        return "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg"; // 기본 YouTube 썸네일 URL
    }

    private String extractYouTubeVideoId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        String pattern = "(?:youtube\\.com/(?:[^/]+/.+/|(?:v|e(?:mbed)?)|.*[?&]v=)|youtu\\.be/|youtube\\.com/shorts/)([^\"&?/ ]{11})";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1); //  YouTube 영상 ID 추출
        }

        return null;
    }

}
