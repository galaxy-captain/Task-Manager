package me.galaxy.task.executor;

import me.galaxy.task.TaskConfigurer;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-20 10:40
 **/
public interface TaskExecuteActor {

    Object submit(Object... arguments);

}