package me.galaxy.task.aop;

import me.galaxy.task.executor.TaskExecuteActor;
import me.galaxy.task.executor.TaskExecutorCenter;
import me.galaxy.task.status.TaskLifeCycle;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.Ordered;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

public abstract class TaskExecutionInterceptor extends TaskExecutionAspectSupport implements MethodInterceptor, Ordered {

    private TaskExecutorCenter taskExecutorCenter;

    private TaskLifeCycle lifeCycle;

    public TaskExecutionInterceptor(TaskExecutorCenter taskExecutorCenter, Executor defaultExecutor, TaskUncaughtExceptionHandler exceptionHandler) {
        super(defaultExecutor, exceptionHandler);
        this.taskExecutorCenter = taskExecutorCenter;
    }

    public void setLifeCycle(TaskLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) {

        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        final Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        // 获取执行任务的线程池
        AsyncTaskExecutor executor = determineTaskExecutor(userDeclaredMethod);

        if (executor == null) {
            throw new IllegalStateException("No executor specified and no default executor set on TaskExecutionInterceptor either");
        }

        Object[] arguments = invocation.getArguments();
        String taskUniqueName = generateTaskUniqueName(invocation);

        // 获取任务
        TaskExecuteActor taskExecuteActor = this.taskExecutorCenter.buildTaskExecuteActor(taskUniqueName, executor, this.lifeCycle);

        Object result = taskExecuteActor.submit(arguments);

        // 提交任务
        return result;
    }

    protected abstract String generateTaskUniqueName(MethodInvocation invocation);

    @Override
    protected Executor getDefaultExecutor(BeanFactory beanFactory) {
        Executor defaultExecutor = super.getDefaultExecutor(beanFactory);
        return (defaultExecutor != null ? defaultExecutor : new SimpleAsyncTaskExecutor());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
