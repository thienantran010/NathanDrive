package com.easypan.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface GlobalInterceptor {
    //verify parameters
    boolean checkParams() default false;

    // verify login status
    boolean checkLogin() default true;

    // verify super administrator
    boolean checkAdmin() default false;
}
