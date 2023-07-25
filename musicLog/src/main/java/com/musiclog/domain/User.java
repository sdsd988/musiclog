package com.musiclog.domain;

import java.util.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String email;

    private String password;

    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Session> sessions = new ArrayList<>();

    public Session addSession(){
        Session session = Session.builder()
                .user(this)
                .build();

        sessions.add(session);

        return session;

    }

    @Builder
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }
}
