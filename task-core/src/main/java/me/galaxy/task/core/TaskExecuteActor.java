package me.galaxy.task.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public abstract class TaskExecuteActor implements Callable<Object>, TaskLifecycle {

    private TaskLifecycle lifecycle;

    public TaskExecuteActor(TaskLifecycle lifecycle) {

        this.lifecycle = lifecycle;

        if (this.lifecycle == null) {
            throw new NullPointerException("Task lifecycle can not be null.");
        }

    }

    public Object execute(Object[] arguments) {
        this.onWait();
        return this.submit(arguments);
    }

    @Override
    public Object call() throws Exception {

        try {

            this.onRunning();

            Object result = this.execute();

            this.onAchieved(result);

            return result;
        } catch (Throwable ex) {

            if (this.canRetry()) {
                return this.call();
            } else {
                this.onBroken(ex);
                throw ex;
            }

        }
    }

    protected abstract Object submit(Object[] arguments);

    protected abstract Object execute() throws Exception;

    protected abstract boolean canRetry();

    @Override
    public void onInitialize(Object[] arguments) {
        this.lifecycle.onInitialize(arguments);
    }

    @Override
    public void onWait() {
        this.lifecycle.onWait();
    }

    @Override
    public void onRunning() {
        this.lifecycle.onRunning();
    }

    @Override
    public void onAchieved(Object result) {
        this.lifecycle.onAchieved(result);
    }

    @Override
    public void onBroken(Throwable ex) {
        this.lifecycle.onBroken(ex);
    }

}
