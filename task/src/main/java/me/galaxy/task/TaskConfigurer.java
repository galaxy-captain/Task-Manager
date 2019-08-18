package me.galaxy.task;

import me.galaxy.task.aop.TaskUncaughtExceptionHandler;

import java.util.concurrent.Executor;

public interface TaskConfigurer {

    Executor getAsyncExecutor();

    TaskUncaughtExceptionHandler getTaskUncaughtExceptionHandler();

}
