package com.musiclog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musiclog.domain.Post;
import com.musiclog.repository.PostRepository;
import com.musiclog.request.PostCreate;
import com.musiclog.request.PostEdit;
import com.musiclog.service.PostService;
import org.aspectj.lang.annotation.After;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @AfterEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청 시 Hello World 출력")
    void test() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper(); // 찾아볼 것

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청 시 title 값은 필수다.")
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
    @DisplayName("/posts 요청 시 DB에 값이 저장된다.")
    void test3() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper(); // 찾아볼 것

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
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
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title("루시")
                .content("조깅")
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


        mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {

        //given
        PostEdit postEdit = PostEdit.builder()
                .title("막차")
                .content("노래 소개")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}",1L)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함될 수 없다.")
    void test11() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("나는 바보입니다")
                .content("반포자이")
                .build();

        ObjectMapper objectMapper = new ObjectMapper(); // 찾아볼 것

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());


    }

}