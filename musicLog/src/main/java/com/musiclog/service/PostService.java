package com.musiclog.service;

import com.musiclog.domain.Post;
import com.musiclog.domain.PostEditor;
import com.musiclog.repository.PostRepository;
import com.musiclog.request.PostCreate;
import com.musiclog.request.PostEdit;
import com.musiclog.request.PostSearch;
import com.musiclog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate){
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

         postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 글입니다."));


        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

    }

    // 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
    // 글이 ->10,000,000 -> DB글 모두 조회하는 경우 -> DB가 뻗을 수 있다.
    // DB -> 애플리케이션 서버로 전달하는 시간, 트래팍바용 등이 많이 발생할 수 있다.

    public List<PostResponse> getList(PostSearch postSearch) {

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public void edit(Long id, PostEdit postEdit){

       Post post =  postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 글입니다."));

       PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();


       PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);

    }


    public void delete(Long id) {

        Post post = postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 글입니다."));

        postRepository.delete(post);
    }
}
