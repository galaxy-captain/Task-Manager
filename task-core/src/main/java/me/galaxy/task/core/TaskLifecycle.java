package me.galaxy.task.core;

public interface TaskLifecycle {

    void onInitialize(Object[] arguments);

    void onWait();

    void onRunning();

    void onAchieved(Object result);

    void onBroken(Throwable ex);

}
