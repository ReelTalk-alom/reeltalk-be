package com.alom.reeltalkbe.service;

import com.alom.reeltalkbe.domain.User;
import com.alom.reeltalkbe.dto.CustomUserDetails;
import com.alom.reeltalkbe.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> res = userRepository.findByUsername(username);
        if (res.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        User user = res.get();
        return new CustomUserDetails(user);
    }

}
