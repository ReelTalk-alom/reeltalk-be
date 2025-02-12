package com.alom.reeltalkbe.user.jwt;

import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱을 위한 ObjectMapper
    private final JwtUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/users/login");  // 로그인 엔드포인트 지정
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // JSON 요청을 읽어와서 username과 password를 추출
            Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
            String username = requestBody.get("username");
            String password = requestBody.get("password");

            System.out.println("로그인 요청 - username: " + username);

            // 스프링 시큐리티에서 username과 password를 검증하기 위해 토큰 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            // AuthenticationManager를 사용하여 인증 진행
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("로그인 요청 JSON 파싱 중 오류 발생", e);
        }
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        Long userId = customUserDetails.getUserId();
        // 토큰 30분 유지
        String token = jwtUtil.createJwt(userId, username, role, 30 * 60 * 1000L);

        // JWT를 응답 헤더에 추가
        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // JWT와 사용자 정보를 JSON 형식으로 응답
            objectMapper.writeValue(response.getWriter(), Map.of(
                    "userId", userId,
                    "username", username,
                    "role", role,
                    "token", token
            ));
        } catch (IOException e) {
            throw new RuntimeException("로그인 성공 응답 JSON 변환 중 오류 발생", e);
        }
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            objectMapper.writeValue(response.getWriter(), Map.of(
                    "error", "로그인 실패: " + failed.getMessage()
            ));
        } catch (IOException e) {
            throw new RuntimeException("로그인 실패 응답 JSON 변환 중 오류 발생", e);
        }
    }

}
