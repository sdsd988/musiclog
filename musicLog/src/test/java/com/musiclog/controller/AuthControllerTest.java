package com.musiclog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musiclog.domain.Session;
import com.musiclog.domain.User;
import com.musiclog.repository.SessionRepository;
import com.musiclog.repository.UserRepository;
import com.musiclog.request.Login;
import com.musiclog.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test1() throws Exception {
        //given

        userRepository.save(User.builder()
                .name("정상윤")
                .email("sdsd98987@gmail.com")
                .password("1234")
                .build());


        Login login = Login.builder()
                .email("sdsd98987@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 생성")
    void test2() throws Exception {

        User user = userRepository.save(User.builder()
                .name("정상윤")
                .email("sdsd98987@gmail.com")
                .password("1234")
                .build());


        Login login = Login.builder()
                .email("sdsd98987@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

//        User loggedInUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException());
        Assertions.assertEquals(1L, user.getSessions().size());
    }

    @Test
    @DisplayName("로그인 성공 후 세션 응답")
    void test3() throws Exception {

        User user = userRepository.save(User.builder()
                .name("정상윤")
                .email("sdsd98987@gmail.com")
                .password("1234")
                .build());


        Login login = Login.builder()
                .email("sdsd98987@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다. /foo")
    void test4() throws Exception {
        //given
        User user = User.builder()
                .name("정상윤")
                .email("sdsd98987@gmail.com")
                .password("1234")
                .build();

        Session session = user.addSession();
        userRepository.save(user);

        //expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test5() throws Exception {
        //given
        User user = User.builder()
                .name("정상윤")
                .email("sdsd98987@gmail.com")
                .password("1234")
                .build();

        Session session = user.addSession();
        userRepository.save(user);

        //expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken()+ "-other")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }
}