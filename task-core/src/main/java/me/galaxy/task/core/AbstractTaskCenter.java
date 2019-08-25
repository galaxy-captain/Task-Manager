package me.galaxy.task.core;


import me.galaxy.task.core.exception.NoTaskException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTaskCenter implements TaskCenter {

    private Map<String, TaskExecuteConfiguration> taskMap = new HashMap<>();

    @Override
    public void registerTask(Object object) {

        TaskExecuteConfiguration config = buildTaskConfig(object);

        if (config == null) {
            throw new NullPointerException("Task execute configuration can not be null.");
        }

        taskMap.put(config.getName(), config);

    }

    @Override
    public TaskExecuteActor buildTask(String name) {

        TaskExecuteConfiguration config = taskMap.get(name);

        if (config == null) {
            throw new NullPointerException("Task execute configuration '" + name + "' is not found.");
        }

        return buildTaskActor(config);
    }

    protected abstract TaskExecuteConfiguration buildTaskConfig(Object object);

    protected abstract TaskExecuteActor buildTaskActor(TaskExecuteConfiguration config);

}
