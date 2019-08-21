package me.galaxy.manager;

import me.galaxy.task.status.TaskStatus;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-21 11:03
 **/
public interface TaskRecordService {

    /**
     * 初始化任务
     */
    String initTaskExecuteStage(String taskName, Object[] arguments, int executeCount, int maxRetryTimes);

    int saveTaskExecuteStage(String taskId, String taskName, TaskStatus status, int executeCount, int maxRetryTimes);

    int saveBrokenTask(String taskId, String taskName);

    int saveAchievedTask(String taskId, String taskName);
}