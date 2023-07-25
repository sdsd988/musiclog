package com.musiclog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ISBN;

import java.util.UUID;

import static java.util.UUID.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    @ManyToOne
    private User user;

    @Builder
    public Session(User user) {
        this.accessToken = randomUUID().toString();
        this.user = user;
    }
}
