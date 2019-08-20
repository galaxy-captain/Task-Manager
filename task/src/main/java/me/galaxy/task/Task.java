package me.galaxy.task;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Task {

    String value() default "";

    String name() default "";

    String executor() default "";

    int retryTimes() default -1;

    Class<? extends Throwable>[] ignorableException() default {};

}
