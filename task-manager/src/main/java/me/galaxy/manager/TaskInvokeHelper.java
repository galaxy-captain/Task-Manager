package me.galaxy.manager;

import me.galaxy.task.executor.TaskExecutorCenter;
import me.galaxy.task.status.TaskLifeCycle;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-20 18:24
 **/
public class TaskInvokeHelper {

    private TaskExecutorCenter center;

    private TaskLifeCycle lifeCycle;

    private AsyncTaskExecutor executor;

    public TaskInvokeHelper(TaskExecutorCenter center) {
        this.center = center;
    }

    public void setCenter(TaskExecutorCenter center) {
        this.center = center;
    }

    public void setLifeCycle(TaskLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public void setExecutor(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    public <T> T invoke(String name, Object... arguments) {
        return (T) this.center.buildTaskExecuteActor(name, this.executor, this.lifeCycle).submit(arguments);
    }

    public Object invoke(String name, AsyncTaskExecutor executor, Object... arguments) {
        return this.center.buildTaskExecuteActor(name, executor, this.lifeCycle).submit(arguments);
    }

    public Object invoke(String name, TaskLifeCycle lifeCycle, Object... arguments) {
        return this.center.buildTaskExecuteActor(name, this.executor, lifeCycle).submit(arguments);
    }

    public Object invoke(String name, AsyncTaskExecutor executor, TaskLifeCycle lifeCycle, Object... arguments) {
        return this.center.buildTaskExecuteActor(name, executor, lifeCycle).submit(arguments);
    }


}