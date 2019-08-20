package me.galaxy.task;

import me.galaxy.task.aop.TaskUncaughtExceptionHandler;
import me.galaxy.task.executor.TaskExecutorCenter;
import me.galaxy.task.status.TaskLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.MethodIntrospector;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Executor;

public class TaskAnnotationBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TaskAnnotationBeanPostProcessor.class);

    private Executor executor;

    private TaskUncaughtExceptionHandler exceptionHandler;

    private TaskExecutorCenter taskExecutorCenter;

    private TaskLifeCycle lifeCycle;

    public TaskAnnotationBeanPostProcessor() {
        setBeforeExistingAdvisors(true);
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public TaskUncaughtExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(TaskUncaughtExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);

        try {
            this.taskExecutorCenter = beanFactory.getBean(TaskExecutorCenter.class);
        } catch (Exception e) {
            this.taskExecutorCenter = new TaskExecutorCenter();
            ((DefaultListableBeanFactory) beanFactory).registerSingleton(this.taskExecutorCenter.getClass().getName(), this.taskExecutorCenter);
        }

        try {
            this.lifeCycle = beanFactory.getBean(TaskLifeCycle.class);
        } catch (Exception e) {
            // ignore
        }

        TaskAnnotationAdvisor advisor = new TaskAnnotationAdvisor(this.taskExecutorCenter, this.lifeCycle, this.executor, this.exceptionHandler);
        advisor.setBeanFactory(beanFactory);

        this.advisor = advisor;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        bean = super.postProcessBeforeInitialization(bean, beanName);
        initializeTaskMethod(bean, beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        bean = super.postProcessAfterInitialization(bean, beanName);

        return bean;
    }

    /**
     * 初始化任务方法
     */
    private void initializeTaskMethod(Object clazz, String beanName) {
        this.taskExecutorCenter.registerTask(clazz);
    }

    private void advanceTaskMethod(Object clazz) {

    }

}
