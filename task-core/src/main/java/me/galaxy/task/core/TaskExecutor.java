package me.galaxy.task.core;

import java.util.concurrent.Future;

public interface TaskExecutor {

    <T> Future<T> submit(TaskExecuteActor actor);

}
