package com.musiclog.config;

import com.musiclog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class MethodSecurityConfig {

    private final PostRepository postRepository;

    @Bean
    public MethodSecurityExpressionHandler expressionHandler(){
        var handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new MusiclogPermissionEvaluator(postRepository));
        return handler;
    }
}
