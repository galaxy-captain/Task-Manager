package me.galaxy.task.aop;

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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class TaskExecutionInterceptor extends TaskExecutionAspectSupport implements MethodInterceptor, Ordered {

    public TaskExecutionInterceptor(Executor defaultExecutor) {
        super(defaultExecutor);
    }

    public TaskExecutionInterceptor(Executor defaultExecutor, TaskUncaughtExceptionHandler exceptionHandler) {
        super(defaultExecutor, exceptionHandler);
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

        Callable<Object> task = new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                try {

                    Object result = invocation.proceed();
                    if (result instanceof Future) {
                        return ((Future<?>) result).get();
                    }

                } catch (ExecutionException ex) {
                    handleError(ex.getCause(), userDeclaredMethod, invocation.getArguments());
                } catch (Throwable ex) {
                    handleError(ex, userDeclaredMethod, invocation.getArguments());
                }

                return null;
            }

        };

        return doSubmit(task, executor, invocation.getMethod().getReturnType());
    }

    protected abstract String generateMethodUniqueKey(Method method);

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
