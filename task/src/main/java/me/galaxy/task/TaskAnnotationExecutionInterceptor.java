package me.galaxy.task;

import me.galaxy.task.aop.TaskExecutionInterceptor;
import me.galaxy.task.aop.TaskUncaughtExceptionHandler;
import me.galaxy.task.executor.TaskExecuteActor;
import me.galaxy.task.executor.TaskExecutorCenter;
import me.galaxy.task.utils.FormatUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

public class TaskAnnotationExecutionInterceptor extends TaskExecutionInterceptor {

    public TaskAnnotationExecutionInterceptor(TaskExecutorCenter taskExecutorCenter, Executor defaultExecutor, TaskUncaughtExceptionHandler exceptionHandler) {
        super(taskExecutorCenter, defaultExecutor, exceptionHandler);
    }

    @Override
    public String generateTaskUniqueName(MethodInvocation invocation) {

        Object clazz = invocation.getThis();
        Method method = invocation.getMethod();
        Task task = method.getAnnotation(Task.class);

        return TaskExecutorCenter.generateTaskUniqueName(clazz, method, task);
    }

    @Override
    protected String getExecutorQualifier(Method method) {

        Task task = AnnotatedElementUtils.findMergedAnnotation(method, Task.class);

        if (task == null) {
            task = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), Task.class);
        }

        return task != null ? task.executor() : null;
    }

}
