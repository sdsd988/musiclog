package com.musiclog.service;

import com.musiclog.domain.Post;
import com.musiclog.repository.PostRepository;
import com.musiclog.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate){
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .contents(postCreate.getContent())
                .build();

         postRepository.save(post);
    }
}
