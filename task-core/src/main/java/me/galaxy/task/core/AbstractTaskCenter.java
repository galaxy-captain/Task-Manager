package me.galaxy.task.core;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTaskCenter implements TaskCenter {

    private Map<String, TaskExecuteConfiguration> taskMap = new HashMap<>();

    @Override
    public void registerTask(Object object) {

        Map<String,TaskExecuteConfiguration> configs= buildTaskConfig(object);

        taskMap.putAll(configs);
    }

    @Override
    public TaskExecuteActor buildTask(String name) {

        TaskExecuteConfiguration config = taskMap.get(name);

        if (config == null) {
            throw new NullPointerException("Task execute configuration '" + name + "' is not found.");
        }

        return buildTaskActor(config);
    }

    protected abstract  Map<String,TaskExecuteConfiguration> buildTaskConfig(Object object);

    protected abstract TaskExecuteActor buildTaskActor(TaskExecuteConfiguration config);

}
