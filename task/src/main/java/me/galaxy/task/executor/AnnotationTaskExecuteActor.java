package me.galaxy.task.executor;

import me.galaxy.task.status.TaskLifeCycle;
import me.galaxy.task.status.TaskStatus;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-20 12:16
 **/
public class AnnotationTaskExecuteActor extends AbstractTaskExecuteActor {

    public AnnotationTaskExecuteActor(TaskExecuteConfig config) {
        setClazz(config.getClazz());
        setMethod(config.getMethod());
        setMaxRetryCount(config.getMaxRetryCount());
        setTaskName(config.getName());
    }

    @Override
    public String getId() {
        return taskUniqueId;
    }

    @Override
    public String getName() {
        return taskName;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public int getExecuteCount() {
        return executeCount;
    }

    @Override
    public int getMaxRetryCount() {
        return maxRetryCount;
    }
}