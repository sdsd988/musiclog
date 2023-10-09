package com.musiclog.config;

import com.musiclog.domain.User;
import com.musiclog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.*;
public class MusiclogMockSecurityContext implements WithSecurityContextFactory<MusiclogMockUser> {

    private UserRepository userRepository;

    public MusiclogMockSecurityContext(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SecurityContext createSecurityContext(MusiclogMockUser annotation) {
        var user = User.builder()
                .email(annotation.email())
                .name(annotation.name())
                .password(annotation.password())
                .build();

        userRepository.save(user);

        var principal = new UserPrincipal(user);

        var role = new SimpleGrantedAuthority("ROLE_ADMIN");
        var authenticationToken = new UsernamePasswordAuthenticationToken(principal,
                user.getPassword(),
                List.of(role));

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);


        return context;
    }


}
