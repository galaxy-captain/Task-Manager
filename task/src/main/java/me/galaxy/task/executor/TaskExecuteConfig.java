package me.galaxy.task.executor;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-20 11:19
 **/
public class TaskExecuteConfig {

    private String name = null;

    private Object clazz = null;

    private Method method = null;

    private int maxRetryCount = 1;

    private Class<? extends Exception>[] ignorableException = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getClazz() {
        return clazz;
    }

    public void setClazz(Object clazz) {
        this.clazz = clazz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public Class<? extends Exception>[] getIgnorableException() {
        return ignorableException;
    }

    public void setIgnorableException(Class<? extends Exception>[] ignorableException) {
        this.ignorableException = ignorableException;
    }

}