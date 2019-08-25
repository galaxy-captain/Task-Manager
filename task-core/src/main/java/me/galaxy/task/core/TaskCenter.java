package me.galaxy.task.core;

public interface TaskCenter {

    void registerTask(Object object);

    TaskExecuteActor buildTask(String name);

}
