package me.galaxy.sample;

import me.galaxy.task.status.TaskLifeCycle;
import me.galaxy.task.status.TaskStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-20 18:12
 **/
public class TaskRecordWithRDS implements TaskLifeCycle {

    @Override
    public String onInitialize(Object clazz, Method method, Object[] arguments) {
        return null;
    }

    @Override
    public void onWait(String taskUniqueId, TaskStatus status) {

    }

    @Override
    public void onRunning(String taskUniqueId, TaskStatus status) {

    }

    @Override
    public void onAchieved(String taskUniqueId, TaskStatus status, Object result) {

    }

    @Override
    public void onBroken(String taskUniqueId, TaskStatus status, Throwable t) {

    }

}