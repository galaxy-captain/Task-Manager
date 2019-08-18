package me.galaxy.task;

import me.galaxy.task.aop.TaskUncaughtExceptionHandler;

import java.util.concurrent.Executor;

public class TaskConfigurerSupport implements TaskConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        return null;
    }

    @Override
    public TaskUncaughtExceptionHandler getTaskUncaughtExceptionHandler() {
        return null;
    }

}
