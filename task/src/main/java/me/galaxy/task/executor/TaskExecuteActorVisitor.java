package me.galaxy.task.executor;

import me.galaxy.task.status.TaskStatus;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-21 16:20
 **/
public interface TaskExecuteActorVisitor {

    String getId();

    String getName();

    TaskStatus getStatus();

    int getExecuteCount();

    int getMaxRetryCount();

}