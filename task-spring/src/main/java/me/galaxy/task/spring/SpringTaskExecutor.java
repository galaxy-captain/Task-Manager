package me.galaxy.task.spring;

import me.galaxy.task.core.TaskExecuteActor;
import me.galaxy.task.core.TaskExecutor;

import java.util.concurrent.Future;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-08-26 12:11
 **/
public class SpringTaskExecutor implements TaskExecutor {

    @Override
    public <T> Future<T> submit(TaskExecuteActor actor) {
        return null;
    }

}