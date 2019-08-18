package me.galaxy.task.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class SimpleTaskUncaughtExceptionHandler implements TaskUncaughtExceptionHandler {

    public static final Logger logger = LoggerFactory.getLogger(SimpleTaskUncaughtExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (logger.isErrorEnabled()) {
            logger.error(String.format("Unexpected error occurred invoking async method '%s'.", method), ex);
        }
    }

}
