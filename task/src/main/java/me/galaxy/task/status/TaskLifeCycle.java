package me.galaxy.task.status;

import me.galaxy.task.executor.AnnotationTaskExecuteActor;
import me.galaxy.task.executor.TaskExecuteActorVisitor;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-19 10:54
 **/
public interface TaskLifeCycle {

//    /**
//     * 初始化状态
//     */
//    String onInitialize(Object clazz, Method method, Object[] arguments);
//
//    /**
//     * 等待状态
//     */
//    void onWait(String taskUniqueId, TaskStatus status);
//
//    /**
//     * 执行状态
//     */
//    void onRunning(String taskUniqueId, TaskStatus status);
//
//    /**
//     * 完成状态
//     */
//    void onAchieved(String taskUniqueId, TaskStatus status, Object result);
//
//    /**
//     * 终止状态
//     */
//    void onBroken(String taskUniqueId, TaskStatus status, Throwable t);

    String onInitialize(TaskExecuteActorVisitor visitor, Object[] arguments);

    void onWait(TaskExecuteActorVisitor visitor);

    void onRunning(TaskExecuteActorVisitor visitor);

    void onAchieved(TaskExecuteActorVisitor visitor, Object result);

    void onBroken(TaskExecuteActorVisitor visitor, Throwable t, Object[] arguments);
}