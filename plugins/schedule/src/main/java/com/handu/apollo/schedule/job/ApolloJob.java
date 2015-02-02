package com.handu.apollo.schedule.job;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by markerking on 14-7-23.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface ApolloJob {
    String value() default "";
}
