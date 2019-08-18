package me.galaxy.task;

import me.galaxy.task.aop.TaskExecutionInterceptor;
import me.galaxy.task.aop.TaskUncaughtExceptionHandler;
import me.galaxy.task.utils.FormatUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

public class TaskAnnotationExecutionInterceptor extends TaskExecutionInterceptor {

    public TaskAnnotationExecutionInterceptor(Executor defaultExecutor) {
        super(defaultExecutor);
    }

    public TaskAnnotationExecutionInterceptor(Executor defaultExecutor, TaskUncaughtExceptionHandler exceptionHandler) {
        super(defaultExecutor, exceptionHandler);
    }

    @Override
    public String generateMethodUniqueKey(Method method) {
        return "[" + method.getDeclaringClass().getName() + "]" + method.getName() + FormatUtils.methodParameterTypesToString(method);
    }

    @Override
    protected String getExecutorQualifier(Method method) {

        Task task = AnnotatedElementUtils.findMergedAnnotation(method, Task.class);

        if (task == null) {
            task = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), Task.class);
        }

        return task != null ? task.value() : null;
    }

}
