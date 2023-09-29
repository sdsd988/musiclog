package com.musiclog.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

public class UserPrincipal extends User {

    private final Long userId;


    public UserPrincipal(com.musiclog.domain.User user) {
        super(user.getEmail(), user.getPassword(),List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
