package com.musiclog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(
        indexes = {
                @Index(name = "IDX_COMMENT_POST_ID",columnList = "post_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String password ;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn
    private Post post;


    @Builder
    public Comment(String author, String password, String content, Post post) {
        this.author = author;
        this.password = password;
        this.content = content;
        this.post = post;
    }
}
