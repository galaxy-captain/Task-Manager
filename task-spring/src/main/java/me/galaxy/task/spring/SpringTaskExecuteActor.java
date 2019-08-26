package me.galaxy.task.spring;

import me.galaxy.task.core.TaskExecuteActor;
import me.galaxy.task.core.TaskExecuteConfiguration;
import me.galaxy.task.core.TaskLifecycle;
import org.springframework.core.task.AsyncTaskExecutor;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-08-26 12:09
 **/
public class SpringTaskExecuteActor extends TaskExecuteActor {

    private TaskExecuteConfiguration config;

    private Object object;

    private Object enhancedObject;

    private Method method;

    private Object[] arguments;

    private AsyncTaskExecutor executor;

    private int retryTimes = 0;

    public SpringTaskExecuteActor(TaskExecuteConfiguration config, AsyncTaskExecutor executor, TaskLifecycle lifecycle) {
        super(lifecycle);
        this.executor = executor;
        this.config = config;
        this.object = config.getObject();
        this.enhancedObject = config.getEnhancedObject();
        this.method = config.getMethod();
    }

    @Override
    protected Object submit(Object[] arguments) {

        this.arguments = arguments;

        return executor.submit(this);
    }

    @Override
    protected Object execute() throws Exception {
        return this.method.invoke(this.enhancedObject, this.arguments);
    }

    @Override
    protected boolean canRetry() {

        if (this.retryTimes < this.config.getRetryTimes()) {
            this.retryTimes++;
            return true;
        }

        return false;
    }

}