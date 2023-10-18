package com.projectmicrosoft.microsoft.api.security;


import java.lang.annotation.*;
import java.util.List;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticatedUser {

    String requiredRole() default "";

}
