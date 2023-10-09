package com.musiclog.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MusiclogMockSecurityContext.class)
public @interface MusiclogMockUser {

    String name() default "정상윤";

    String email() default "sdsd98987@gmail.com";

    String password() default "";

//    String role() default "ROLE_ADMIN";
}
