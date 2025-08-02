package com.alom.reeltalkbe.domain.oauth.service;



import com.alom.reeltalkbe.domain.oauth.dto.GoogleResponse;
import com.alom.reeltalkbe.domain.oauth.dto.NaverResponse;
import com.alom.reeltalkbe.domain.oauth.dto.OAuth2Response;
import com.alom.reeltalkbe.domain.user.domain.User;
import com.alom.reeltalkbe.domain.user.dto.CustomUserDetails;
import com.alom.reeltalkbe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationID = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationID.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if(registrationID.equals("google")){
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else{
            return null;
        }
        String username = oAuth2Response.getUserName()+"_"+ oAuth2Response.getProviderId();     //username 홍길동_naver
        String password = oAuth2Response.getEmail()+"_"+oAuth2Response.getProviderId();         //password xxx@naver.com_naver

        Optional<User> existUser = userRepository.findByUsername(username);

        if(existUser.isEmpty()){
            User user = User.builder()
                    .email(oAuth2Response.getEmail())
                    .password(bCryptPasswordEncoder.encode(password))
                    .username(username)
                    .role("ROLE_ADMIN")
                    .build();
            user.setDescription("안녕하세요! 반갑습니다.");

            return new CustomUserDetails(userRepository.save(user));

        } else{
            return new CustomUserDetails(existUser.get());
        }

    }
}
