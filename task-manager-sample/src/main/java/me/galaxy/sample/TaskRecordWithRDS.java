package me.galaxy.sample;

import me.galaxy.manager.TaskRecordService;
import me.galaxy.task.executor.TaskExecuteActorVisitor;
import me.galaxy.task.status.TaskLifeCycle;
import me.galaxy.task.status.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-20 18:12
 **/
public class TaskRecordWithRDS implements TaskLifeCycle {

    @Autowired
    private TaskRecordService taskRecordService;

    @Transactional
    @Override
    public String onInitialize(TaskExecuteActorVisitor visitor, Object[] arguments) {
        return taskRecordService.initTaskExecuteStage(
                visitor.getName(),
                arguments,
                visitor.getExecuteCount(),
                visitor.getMaxRetryCount()
        );
    }

    @Override
    public void onWait(TaskExecuteActorVisitor visitor) {
        taskRecordService.saveTaskExecuteStage(
                visitor.getId(),
                visitor.getName(),
                TaskStatus.WAIT,
                visitor.getExecuteCount(),
                visitor.getMaxRetryCount()
        );
    }

    @Override
    public void onRunning(TaskExecuteActorVisitor visitor) {
        taskRecordService.saveTaskExecuteStage(
                visitor.getId(),
                visitor.getName(),
                TaskStatus.RUNNING,
                visitor.getExecuteCount(),
                visitor.getMaxRetryCount()
        );
    }

    @Override
    public void onAchieved(TaskExecuteActorVisitor visitor, Object result) {
        taskRecordService.saveTaskExecuteStage(
                visitor.getId(),
                visitor.getName(),
                TaskStatus.ACHIEVED,
                visitor.getExecuteCount(),
                visitor.getMaxRetryCount()
        );
    }

    @Override
    public void onBroken(TaskExecuteActorVisitor visitor, Throwable t, Object[] arguments) {
        taskRecordService.saveTaskExecuteStage(
                visitor.getId(),
                visitor.getName(),
                TaskStatus.BROKEN,
                visitor.getExecuteCount(),
                visitor.getMaxRetryCount()
        );
    }

}