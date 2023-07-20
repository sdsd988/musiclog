package com.musiclog.service;

import com.musiclog.domain.Post;
import com.musiclog.exception.PostNotFound;
import com.musiclog.repository.PostRepository;
import com.musiclog.request.PostCreate;
import com.musiclog.request.PostEdit;
import com.musiclog.request.PostSearch;
import com.musiclog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();


        //when

        postService.write(postCreate);

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {

        //given

        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(requestPost);

        //when
        PostResponse response = postService.get(requestPost.getId());

        //then
        assertNotNull(response);
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());

    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3() {

        //given
        List<Post> requestPosts = IntStream.range(0,20)
                .mapToObj(i-> Post.builder()
                            .title("음악 제목 " + i)
                            .content("노래 소개   " + i)
                            .build()
                )
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        //when

        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(10L, posts.size());
        assertEquals("음악 제목 19",posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        //given
        Post post = Post.builder().title("음악 제목")
                .content("노래 소개")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("막차")
                .content("노래 소개")
                .build();
        //when

        postService.edit(post.getId(),postEdit);

        //then

        Post changePost = postRepository.findById(post.getId()).orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id= " + post.getId()));

        assertEquals("막차",changePost.getTitle());
        assertEquals("노래 소개",changePost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        //given
        Post post = Post.builder().title("음악 제목")
                .content("노래 소개")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("브로콜리")
                .build();
        //when

        postService.edit(post.getId(),postEdit);

        //then

        Post changePost = postRepository.findById(post.getId()).orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id= " + post.getId()));

        assertEquals("음악 제목",changePost.getTitle());
        assertEquals("브로콜리",changePost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6(){
        //given
        Post post = Post.builder()
                .title("루시")
                .content("조깅")
                .build();

        postRepository.save(post);

        //when

        postService.delete(post.getId());

        //then
        assertEquals(0, postRepository.count());

    }

    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    void test7() {

        //given

        Post post = Post.builder()
                .title("루시")
                .content("조깅")
                .build();

        postRepository.save(post);

        //expected
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });

    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test8() {

        //given
        Post post = Post.builder()
                .title("루시")
                .content("조깅")
                .build();

        postRepository.save(post);


        //expected
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });

    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test9() {
        //given
        Post post = Post.builder().title("음악 제목")
                .content("노래 소개")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .content("브로콜리")
                .build();
        //when


        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId()+1L,postEdit);
        });
    }



}