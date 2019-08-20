package me.galaxy.task;

import me.galaxy.task.aop.SimpleTaskUncaughtExceptionHandler;
import me.galaxy.task.aop.TaskUncaughtExceptionHandler;
import me.galaxy.task.executor.TaskExecuteActor;
import me.galaxy.task.executor.TaskExecutorCenter;
import me.galaxy.task.status.TaskLifeCycle;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executor;

public class TaskAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private TaskUncaughtExceptionHandler exceptionHandler;

    private TaskAnnotationExecutionInterceptor advice;

    private Pointcut pointcut;

    public TaskAnnotationAdvisor(TaskExecutorCenter taskExecutorCenter, TaskLifeCycle lifeCycle, Executor executor, TaskUncaughtExceptionHandler exceptionHandler) {
        Set<Class<? extends Annotation>> asyncAnnotationTypes = new LinkedHashSet<Class<? extends Annotation>>(2);
        asyncAnnotationTypes.add(Task.class);

        if (exceptionHandler != null) {
            this.exceptionHandler = exceptionHandler;
        } else {
            this.exceptionHandler = new SimpleTaskUncaughtExceptionHandler();
        }

        this.advice = buildAdvice(taskExecutorCenter, lifeCycle, executor, this.exceptionHandler);
        this.pointcut = buildPointcut(asyncAnnotationTypes);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice != null) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    /**
     * 构建增强方法
     */
    protected TaskAnnotationExecutionInterceptor buildAdvice(TaskExecutorCenter taskExecutorCenter, TaskLifeCycle lifeCycle, Executor executor, TaskUncaughtExceptionHandler exceptionHandler) {
        TaskAnnotationExecutionInterceptor interceptor = new TaskAnnotationExecutionInterceptor(taskExecutorCenter, executor, exceptionHandler);
        interceptor.setLifeCycle(lifeCycle);
        return interceptor;
    }

    /**
     * 根据注解构建切面位置
     */
    protected Pointcut buildPointcut(Set<Class<? extends Annotation>> asyncAnnotationTypes) {
        ComposablePointcut result = null;
        for (Class<? extends Annotation> asyncAnnotationType : asyncAnnotationTypes) {
            Pointcut cpc = new AnnotationMatchingPointcut(asyncAnnotationType, true);
            Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(asyncAnnotationType);
            if (result == null) {
                result = new ComposablePointcut(cpc).union(mpc);
            } else {
                result.union(cpc).union(mpc);
            }
        }
        return result;
    }

}
