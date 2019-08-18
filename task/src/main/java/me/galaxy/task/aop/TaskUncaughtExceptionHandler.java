package me.galaxy.task.aop;

import java.lang.reflect.Method;

public interface TaskUncaughtExceptionHandler {

    void handleUncaughtException(Throwable ex, Method method, Object... params);

}
