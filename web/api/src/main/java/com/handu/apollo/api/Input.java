package com.handu.apollo.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface Input {

    String description() default "";

    boolean required() default false;

    BaseCmd.CommandType type() default BaseCmd.CommandType.OBJECT;

    BaseCmd.CommandType collectionType() default BaseCmd.CommandType.OBJECT;

    boolean expose() default true;

    int length() default Integer.MAX_VALUE;

    String since() default "0.1.0";
}