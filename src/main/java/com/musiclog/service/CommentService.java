package com.musiclog.service;

import com.musiclog.domain.Comment;
import com.musiclog.domain.Post;
import com.musiclog.exception.CommentNotFound;
import com.musiclog.exception.PostNotFound;
import com.musiclog.repository.comment.CommentRepository;
import com.musiclog.repository.post.PostRepository;
import com.musiclog.request.comment.CommentCreate;
import com.musiclog.request.comment.CommentDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void write(Long postId, CommentCreate request) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        Comment comment = Comment.builder()
                .post(post)
                .author(request.getAuthor())
                .password(request.getPassword())
                .content(request.getContent())
                .build();

        post.addComment(comment);

    }

    public void delete(Long commentId, CommentDelete request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFound::new);


        commentRepository.delete(comment);
    }
}
