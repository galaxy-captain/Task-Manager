package me.galaxy.task.core;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Task {

    String value() default "";

    String name() default "";

    int retryTimes() default 0;

    Class<? extends Throwable>[] ignore() default {};

}
