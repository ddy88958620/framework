package com.handu.apollo.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Output {

    String description() default "";

    Class<?> responseObject() default Object.class;

    String since() default "0.1.0";
}