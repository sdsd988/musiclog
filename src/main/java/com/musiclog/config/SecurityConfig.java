package com.musiclog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musiclog.config.handler.Http401Handler;
import com.musiclog.config.handler.Http403Handler;
import com.musiclog.config.handler.LoginFailHandler;
import com.musiclog.domain.User;
import com.musiclog.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;


@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers("/favicon.ico", "/error")
                .requestMatchers(toH2Console());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests()
                    .requestMatchers("/auth/login","/auth/signup").permitAll()
                    .requestMatchers("/user").hasRole("USER")
                    .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/auth/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginProcessingUrl("/auth/login")
                    .defaultSuccessUrl("/")
                    .failureHandler(new LoginFailHandler(objectMapper))
                .and()
                .exceptionHandling(e->{
                    e.accessDeniedHandler(new Http403Handler(objectMapper));
                    e.authenticationEntryPoint(new Http401Handler(objectMapper));
                })
                .rememberMe(rm -> rm.rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(259200)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {

        return username -> {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));

            return new UserPrincipal(user);
        };
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//
//        UserDetails user = User.withUsername("sy")
//                .password("1234")
//                .roles("ADMIN")
//                .build();
//
//        manager.createUser(user);
//
//        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new SCryptPasswordEncoder(
                16,8,1,32,64
        );
    }

}
