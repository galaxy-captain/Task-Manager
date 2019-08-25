package me.galaxy.task.core;

import java.lang.reflect.Method;

public class TaskExecuteConfiguration {

    private Object object;

    private Object enhancedObject;

    private Method method;

    private String name;

    private int retryTimes;

    private Class<? extends Exception>[] ignorableException;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getEnhancedObject() {
        return enhancedObject;
    }

    public void setEnhancedObject(Object enhancedObject) {
        this.enhancedObject = enhancedObject;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Class<? extends Exception>[] getIgnorableException() {
        return ignorableException;
    }

    public void setIgnorableException(Class<? extends Exception>[] ignorableException) {
        this.ignorableException = ignorableException;
    }
}
