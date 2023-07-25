package com.musiclog.controller;


import com.musiclog.domain.User;
import com.musiclog.exception.InvalidRequest;
import com.musiclog.exception.InvalidSigninInformation;
import com.musiclog.repository.UserRepository;
import com.musiclog.request.Login;
import com.musiclog.response.SessionResponse;
import com.musiclog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login){

        String accessToken = authService.signIn(login);

        return new SessionResponse(accessToken);

    }
}
