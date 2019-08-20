package me.galaxy.task.executor;

import me.galaxy.task.status.TaskLifeCycle;
import me.galaxy.task.status.TaskStatus;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.lang.UsesJava8;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Method;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-20 10:44
 **/
public abstract class AbstractTaskExecuteActor implements TaskExecuteActor {

    private static final int DEFAULT_RETRY_COUNT = 1;

    private String taskUniqueId;

    protected Object clazz;

    protected Method method;

    protected int maxRetryCount;

    /**
     * 任务执行器
     */
    protected AsyncTaskExecutor executor;

    /**
     * 任务生命周期回调
     */
    private TaskLifeCycle lifeCycle;

    /**
     * 任务状态
     */
    private TaskStatus status;

    /**
     * 任务执行次数
     */
    private int executeCount;

    public AbstractTaskExecuteActor() {
        this.status = TaskStatus.INIT;
        this.maxRetryCount = DEFAULT_RETRY_COUNT;
        this.executeCount = 0;
    }

    public void setClazz(Object clazz) {
        this.clazz = clazz;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setMaxRetryCount(int maxRetryCount) {

        if (maxRetryCount < 0) {
            return;
        }

        this.maxRetryCount = maxRetryCount;
    }

    public void setExecutor(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    public void setLifeCycle(TaskLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    /**
     * 任务提交到执行器
     */
    @Override
    public Object submit(Object... arguments) {

        if (this.lifeCycle != null) {
            this.taskUniqueId = this.lifeCycle.onInitialize(this.clazz, this.method, arguments);
        }

        if (this.lifeCycle != null) {
            this.lifeCycle.onWait(this.taskUniqueId, this.status);
        }

        return doSubmit(
                this.executor,
                this.buildTask(arguments),
                this.method.getReturnType()
        );
    }

    /**
     * 任务开始执行
     */
    public Object execute(Object... arguments) throws Exception {

        if (this.lifeCycle != null) {
            this.lifeCycle.onRunning(taskUniqueId, this.status);
        }
        this.status = TaskStatus.RUNNING;

        try {
            Object result = AopUtils.invokeJoinpointUsingReflection(this.clazz, this.method, arguments);
            if (result instanceof Future) {
                return ((Future<?>) result).get();
            }

            this.finish(result, arguments);
        } catch (ExecutionException ex) {
            this.handleError(ex.getCause(), this.method, arguments);
        } catch (Throwable ex) {
            this.handleError(ex, this.method, arguments);
        }
        return null;

    }

    /**
     * 任务重试
     */
    public void retry(Object... arguments) {
        this.submit(arguments);
    }

    /**
     * 任务完成
     */
    public void finish(Object result, Object... arguments) {

        if (this.lifeCycle != null) {
            this.lifeCycle.onAchieved(this.taskUniqueId, this.status, result);
        }

        this.status = TaskStatus.ACHIEVE;
    }

    /**
     * 任务终止
     */
    public void terminate(Throwable ex, Object... arguments) throws Exception {
        if (Future.class.isAssignableFrom(method.getReturnType())) {
            ReflectionUtils.rethrowException(ex);
        } else {
            if (this.lifeCycle != null) {
                this.lifeCycle.onBroken(this.taskUniqueId, this.status, ex);
            }
        }
    }

    /**
     * 构建任务
     */
    protected Callable<Object> buildTask(Object... arguments) {
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return execute(arguments);
            }
        };
    }

    /**
     * 处理异常情况
     */
    protected void handleError(Throwable ex, Method method, Object... params) throws Exception {

        if (this.executeCount < this.maxRetryCount) {
            this.executeCount++;
            this.retry(params);
        } else {
            this.terminate(ex, params);
        }
    }

    /**
     * 提交任务到执行器
     */
    private Object doSubmit(AsyncTaskExecutor executor, Callable<Object> task, Class<?> returnType) {

        if (CompletableFutureDelegate.completableFuturePresent) {
            Future<Object> result = CompletableFutureDelegate.processCompletableFuture(returnType, task, executor);
            if (result != null) {
                return result;
            }
        }

        if (ListenableFuture.class.isAssignableFrom(returnType)) {
            return ((AsyncListenableTaskExecutor) executor).submitListenable(task);
        } else if (Future.class.isAssignableFrom(returnType)) {
            return executor.submit(task);
        } else {
            executor.submit(task);
            return null;
        }

    }

    @UsesJava8
    private static class CompletableFutureDelegate {

        static final boolean completableFuturePresent = ClassUtils.isPresent(
                "java.util.concurrent.CompletableFuture",
                CompletableFutureDelegate.class.getClassLoader()
        );

        static <T> Future<T> processCompletableFuture(Class<?> returnType, final Callable<T> task, Executor executor) {

            if (!CompletableFuture.class.isAssignableFrom(returnType)) {
                return null;
            }

            return CompletableFuture.supplyAsync(new Supplier<T>() {
                @Override
                public T get() {
                    try {
                        return task.call();
                    } catch (Throwable ex) {
                        throw new CompletionException(ex);
                    }
                }
            }, executor);

        }

    }

}