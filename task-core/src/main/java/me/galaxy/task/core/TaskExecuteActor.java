package me.galaxy.task.core;

import java.util.concurrent.Callable;

public abstract class TaskExecuteActor implements Callable<Object>, TaskLifecycle {

    private TaskLifecycle lifecycle;

    private TaskExecutor executor;

    public TaskExecuteActor(TaskExecutor executor, TaskLifecycle lifecycle) {
        this.executor = executor;
        this.lifecycle = lifecycle;

        if (this.executor == null) {
            throw new NullPointerException("Task executor can not be null.");
        }

        if (this.lifecycle == null) {
            throw new NullPointerException("Task lifecycle can not be null.");
        }

    }

    public void submit(Object[] arguments) {
        this.onWait();
        this.executor.submit(this);
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
