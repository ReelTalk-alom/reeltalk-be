package com.alom.reeltalkbe.user.service;

import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.user.domain.RefreshEntity;
import com.alom.reeltalkbe.user.jwt.JwtUtil;
import com.alom.reeltalkbe.user.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    // Refresh 토큰을 검증하고 새 토큰을 발급하는 메서드
    public BaseResponse<?> reissueToken(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에서 Refresh 토큰 가져오기
        String refresh = extractRefreshToken(request);
        if (refresh == null) {
            throw new BaseException(BaseResponseStatus.NO_TOKEN);
        }

        // Refresh 토큰 검증
        if (!validateRefreshToken(refresh)) {
            throw new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);
        }

        // DB에서 Refresh 토큰 존재 여부 확인
        if (!refreshRepository.existsByRefresh(refresh)) {
            throw new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);
        }

        // 새로운 Access & Refresh 토큰 생성
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        Long userId = jwtUtil.getUserId(refresh);

        String newAccess = jwtUtil.createJwt("access", userId, username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", userId, username, role, 86400000L);

        // 기존 Refresh 토큰 삭제 후 새로운 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        saveRefreshToken(username, newRefresh, 86400000L);

        // 새로운 토큰을 응답에 추가
        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        // 응답 데이터 구성
        Map<String, String> result = new HashMap<>();
        result.put("accessToken", newAccess);
        result.put("refreshToken", newRefresh);

        return new BaseResponse<>(result);
    }

    // Refresh 토큰을 쿠키에서 추출하는 메서드
    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Refresh 토큰 유효성 검증
    private boolean validateRefreshToken(String refresh) {
        try {
            if (jwtUtil.isExpired(refresh)) {
                return false;
            }
            return "refresh".equals(jwtUtil.getCategory(refresh));
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    // 새로운 Refresh 토큰을 DB에 저장하는 메서드
    private void saveRefreshToken(String username, String refresh, Long expiredMs) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(expirationDate.toString());

        refreshRepository.save(refreshEntity);
    }

    // Refresh 토큰을 저장하는 쿠키 생성
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}