package com.handu.apollo.api;

import com.handu.apollo.api.response.ResponseObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface ApiCommand {
    Class<? extends ResponseObject> responseObject();

    String name() default "";

    String description() default "";

    String usage() default "";

    String group() default "";

    String groupName() default "";

    String since() default "0.1.0";
}