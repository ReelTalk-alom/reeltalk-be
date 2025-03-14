package com.alom.reeltalkbe.content.service;

import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.dto.TMDB.TMDBMovieDetailsRequest;
import com.alom.reeltalkbe.content.dto.TMDB.TMDBMovieResponse;
import com.alom.reeltalkbe.content.dto.TMDB.TMDBSeriesDetailsRequest;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TMDBService {

    private final ContentRepository contentRepository;
    private final RestTemplate restTemplate;
    private static final String TMDB_API_KEY = "023d161ee083b158d3fb00e7c93f6687";
    private static final String[] TMDB_SERIES_URL = {
            "https://api.themoviedb.org/3/tv/top_rated?api_key=" + TMDB_API_KEY + "&language=ko-KR&page=1",
            "https://api.themoviedb.org/3/tv/airing_today?api_key=" + TMDB_API_KEY + "&language=ko-KR&page=1",
            "https://api.themoviedb.org/3/tv/on_the_air?api_key=" + TMDB_API_KEY + "&language=ko-KR&page=1"
    };

    private static final String[] TMDB_MOVIES_URL = {
        "https://api.themoviedb.org/3/movie/top_rated?api_key=" + TMDB_API_KEY + "&language=ko-KR&page=1",
        "https://api.themoviedb.org/3/movie/now_playing?api_key=" + TMDB_API_KEY + "&language=ko-KR&page=1",
        "https://api.themoviedb.org/3/movie/upcoming?api_key=" + TMDB_API_KEY + "&language=ko-KR&page=1"
    };

    // 비동기 실행
    @Async
    public CompletableFuture<List<TMDBSeriesDetailsRequest>> fetchLatestSeriesFromTMDB() {
        List<TMDBSeriesDetailsRequest> allSeries = new ArrayList<>();

        for (String url : TMDB_SERIES_URL) {
            try {
                TMDBResponse response = restTemplate.getForObject(url, TMDBResponse.class);

                if (response != null && response.getResults() != null) {
                    List<CompletableFuture<TMDBSeriesDetailsRequest>> futures = response.getResults().stream()
                            .map(request -> fetchSeriesDetailsFromTMDB(request.getId())
                                    .exceptionally(ex -> {
                                        System.err.println("비동기 TMDB 요청 실패: " + request.getId() + " - " + ex.getMessage());
                                        return null;
                                    })
                            )
                            .collect(Collectors.toList());

                    CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
                    allFutures.join();

                    List<TMDBSeriesDetailsRequest> detailedRequests = futures.stream()
                            .map(CompletableFuture::join)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    allSeries.addAll(detailedRequests);
                }
            } catch (RestClientException e) {
                System.err.println("TMDB API 요청 실패: " + url + " - " + e.getMessage());
            }
        }

        return CompletableFuture.completedFuture(allSeries);
    }

    @Async
    public CompletableFuture<TMDBSeriesDetailsRequest> fetchSeriesDetailsFromTMDB(Long seriesId) {
        String detailsUrl = "https://api.themoviedb.org/3/tv/" + seriesId + "?api_key=" + TMDB_API_KEY + "&language=ko-KR";

        try {
            TMDBSeriesDetailsRequest details = restTemplate.getForObject(detailsUrl, TMDBSeriesDetailsRequest.class);
            return CompletableFuture.completedFuture(details);
        } catch (RestClientException e) {
            System.err.println("TMDB 시리즈 상세 정보 가져오기 실패: " + seriesId + " - " + e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    // 영화 업데이트
    @Async
    public CompletableFuture<List<TMDBMovieDetailsRequest>> fetchLatestMoviesFromTMDB() {
        List<TMDBMovieDetailsRequest> allMovieSummaries = new ArrayList<>();

        for (String url : TMDB_MOVIES_URL) {
            try {
                TMDBMovieResponse response = restTemplate.getForObject(url, TMDBMovieResponse.class);
                if (response != null && response.getResults() != null)
                  allMovieSummaries.addAll(response.getResults());
                } catch (RestClientException e) {
                System.err.println("TMDB 영화 API 요청 실패: " + url + " - " + e.getMessage());
            }
        }

        // 60개 전체 & DB에서의 중복 제거 로직
        Set<Long> uniqueMovieIds = allMovieSummaries.stream()
            .map(TMDBMovieDetailsRequest::getId)
            .collect(Collectors.toSet());
        Set<Long> existingMovieIds = contentRepository.findAllById(uniqueMovieIds)
            .stream()
            .map(Content::getId) // entity에서 영화 ID 추출
            .collect(Collectors.toSet());
        List<Long> newMovieIds = uniqueMovieIds.stream()
            .filter(movieId -> !existingMovieIds.contains(movieId))
            .toList();

        List<CompletableFuture<TMDBMovieDetailsRequest>> futures = newMovieIds.stream()
              .map(movieId -> fetchMovieDetailsFromTMDB(movieId)
                  .exceptionally(ex -> {
                    System.err.println("비동기 TMDB 영화 요청 실패: " + movieId + " - " + ex.getMessage());
                    return null;
                  }))
              .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return CompletableFuture.completedFuture(
                futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<TMDBMovieDetailsRequest> fetchMovieDetailsFromTMDB(Long movieId) {
        String detailsUrl = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + TMDB_API_KEY + "&language=ko-KR";
        String forOverview = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + TMDB_API_KEY;
        try {
              TMDBMovieDetailsRequest details = restTemplate.getForObject(detailsUrl, TMDBMovieDetailsRequest.class);
              if (details != null && Objects.equals(details.getOverview(), "")) {
                details.setOverview(
                    Objects.requireNonNull(restTemplate.getForObject(forOverview, TMDBMovieDetailsRequest.class)).getOverview());
              }

              return CompletableFuture.completedFuture(details);
              } catch (RestClientException e) {
                  System.err.println("TMDB 영화 상세 정보 가져오기 실패: " + movieId + " - " + e.getMessage());
              }

              return CompletableFuture.completedFuture(null);
    }



  // dto
    private static class TMDBResponse {
        private List<TMDBSeriesDetailsRequest> results;

        public List<TMDBSeriesDetailsRequest> getResults() {
            return results;
        }
    }
}
