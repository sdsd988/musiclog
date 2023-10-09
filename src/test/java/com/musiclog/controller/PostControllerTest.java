package com.musiclog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musiclog.config.MusiclogMockUser;
import com.musiclog.domain.Post;
import com.musiclog.domain.User;
import com.musiclog.repository.UserRepository;
import com.musiclog.repository.post.PostRepository;
import com.musiclog.request.post.PostCreate;
import com.musiclog.request.post.PostEdit;
import com.musiclog.service.PostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;
    @AfterEach
    void clean(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }



    @Test
    @DisplayName("글 작성 요청 시 title 값은 필수다.")
    void test2() throws Exception {

        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper(); // 찾아볼 것

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400")) //jsonPath 공부 할 것 object, array 검증
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요!"))
                .andDo(print());
    }

    @Test
    @MusiclogMockUser
//    @WithMockUser(username = "sdsd98987@gmail.com" , roles = {"ADMIN"})
    @DisplayName("글 작성")
    void test3() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();


        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                        .header("authorization","sy")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }


    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);

        //클라이언트 요구사항 제목 10제한

        //expected
        mockMvc.perform(get("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());

    }


    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        Post post1 = postRepository.save(Post.builder()
                .title("title_1")
                .content("content_1")
                .build());

        Post post2 = postRepository.save(Post.builder()
                .title("title_2")
                .content("content_2")
                .build());
        //클라이언트 요구사항 제목 10제한

        //expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id").value(post2.getId()))
                .andExpect(jsonPath("$[0].title").value("title_2"))
                .andExpect(jsonPath("$[0].content").value("content_2"))
                .andExpect(jsonPath("$[1].id").value(post1.getId()))
                .andExpect(jsonPath("$[1].title").value("title_1"))
                .andExpect(jsonPath("$[1].content").value("content_1"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 여러개 조회 페이징")
    void test6() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0,20)
                .mapToObj(i-> Post.builder()
                        .title("음악 제목 " + i)
                        .content("노래 소개 " + i)
                        .build()
                )
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                    .andExpect(jsonPath("$[0].id").value(requestPosts.get(19).getId()))
                .andExpect(jsonPath("$[0].title").value("음악 제목 19"))
                .andExpect(jsonPath("$[0].content").value("노래 소개 19"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "sdsd98987@gmail.com" ,
            roles = {"ADMIN"})
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        //given
        Post post = Post.builder().title("음악 제목")
                .content("노래 소개")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("막차")
                .content("노래 소개")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postEdit)))

                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @MusiclogMockUser
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        //given


        User user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("루시")
                .content("조깅")
                .user(user)
                .build();

        postRepository.save(post);

        //expected

        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {


        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    @WithMockUser(username = "sdsd98987@gmail.com" ,
            roles = {"ADMIN"})
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {

        //given
        PostEdit postEdit = PostEdit.builder()
                .title("막차")
                .content("노래 소개")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}",3L)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


}