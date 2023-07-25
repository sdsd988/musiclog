package com.musiclog.service;

import com.musiclog.domain.Session;
import com.musiclog.domain.User;
import com.musiclog.exception.InvalidSigninInformation;
import com.musiclog.repository.UserRepository;
import com.musiclog.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signIn(Login login) {
        User user =  userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        Session session = user.addSession();

        return session.getAccessToken();
    }
}
