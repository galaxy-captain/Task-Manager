package me.galaxy.task.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-19 18:22
 **/
public class DefaultTaskLifeCycle implements TaskLifeCycle {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTaskLifeCycle.class);

    @Override
    public String onInitialize(Object clazz, Method method, Object[] arguments) {
        return System.currentTimeMillis() + "";
    }

    @Override
    public void onWait(String taskUniqueId, TaskStatus status) {
        String msg = String.format("id=%s,status=%s", taskUniqueId, "onWait");
        System.out.println(msg);
    }

    @Override
    public void onRunning(String taskUniqueId, TaskStatus status) {
        String msg = String.format("id=%s,status=%s", taskUniqueId, "onRunning");
        System.out.println(msg);
    }

    @Override
    public void onAchieved(String taskUniqueId, TaskStatus status, Object result) {
        String msg = String.format("id=%s,status=%s", taskUniqueId, "onAchieved");
        System.out.println(msg);
    }

    @Override
    public void onBroken(String taskUniqueId, TaskStatus status, Throwable t) {
        String msg = String.format("id=%s,status=%s", taskUniqueId, "onBroken");
        System.out.println(msg);
        t.printStackTrace();
    }

}