package com.musiclog.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Signup {

    private String email;
    private String password;
    private String name;

    public Signup(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Signup() {
    }
}


