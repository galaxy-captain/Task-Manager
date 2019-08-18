package me.galaxy.task;

import me.galaxy.task.aop.TaskUncaughtExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;

import java.util.concurrent.Executor;

public class TaskAnnotationBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TaskAnnotationBeanPostProcessor.class);

    private Executor executor = null;

    private TaskUncaughtExceptionHandler exceptionHandler = null;

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

        TaskAnnotationAdvisor advisor = new TaskAnnotationAdvisor(this.executor, this.exceptionHandler);
        advisor.setBeanFactory(beanFactory);

        this.advisor = advisor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        bean = super.postProcessAfterInitialization(bean, beanName);

        initializeTaskMethod(bean, beanName);

        return bean;
    }

    private void initializeTaskMethod(Object clazz, String beanName) {

    }

}
