package com.musiclog.controller;

import com.musiclog.request.PostCreate;
import com.musiclog.request.PostEdit;
import com.musiclog.request.PostSearch;
import com.musiclog.response.PostResponse;
import com.musiclog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    //데이터를 검증하는 이유

    //1. client 개발자가 실수할 수 있다. 실수로 값을 안보낼 수 있다.
    //2. client 버그로 값이 누락될 수 있다.
    //3. 외부에서 나쁜 의도로 값을 조작해서 보낼 수 있다.
    //4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
    //5. 서버 개발자의 편안함을 위해서
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request)  {
        //Case1. 저장한 데이터 Entity -> response로 응답하기
        //Case2. 저장한 데이터의 primary_id -> response로 응답하기
        request.validate();
         postService.write(request);
    }

    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {

        return postService.get(postId);
    }

    //조회 API
    //지난 시간 = 단건 조회 API (1개의 글 post를 가져오는 기능)
    //여러개의 글을 조회 API
    // /posts

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch){
        return postService.getList(postSearch);
    }


    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }


}
