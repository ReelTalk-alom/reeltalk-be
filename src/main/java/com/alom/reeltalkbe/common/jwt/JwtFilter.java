package com.alom.reeltalkbe.common.jwt;

import com.alom.reeltalkbe.domain.User;
import com.alom.reeltalkbe.dto.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("doFilterInternal");
        String requestURI = request.getRequestURI();

//        // 회원가입 & 로그인 요청은 필터 적용 안 함
//        if (requestURI.equals("/api/users/signup") || requestURI.equals("/api/users/login")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        // 요청 Body에서 JSON 파싱해서 "token" 값 가져오기
        String token = extractToken(request);

        // 토큰이 없으면 그대로 필터 체인 실행
        if (token == null) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("authorization now");

        // 토큰이 만료되었는지 확인
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 username과 role 가져오기
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // userEntity 객체 생성 및 값 설정
        User userEntity = new User();
        userEntity.setUsername(username);
        userEntity.setPassword("temppassword");
        userEntity.setRole(role);

        // UserDetails 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities()
        );

        // SecurityContextHolder에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        // HTTP Header에서 "Authorization" 가져오기
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // "Bearer " 이후의 토큰 값만 추출
        }

        return null; // 토큰이 없으면 null 반환
    }
}