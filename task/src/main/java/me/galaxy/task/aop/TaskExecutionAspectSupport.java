package me.galaxy.task.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.*;

public abstract class TaskExecutionAspectSupport implements BeanFactoryAware {

    protected final Logger logger = LoggerFactory.getLogger(TaskExecutionAspectSupport.class);

    public static final String DEFAULT_TASK_EXECUTOR_BEAN_NAME = "taskExecutor";

    private final Map<Method, AsyncTaskExecutor> executors = new ConcurrentHashMap<Method, AsyncTaskExecutor>(16);

    private volatile Executor defaultExecutor;

    private TaskUncaughtExceptionHandler exceptionHandler;

    private BeanFactory beanFactory;

    public TaskExecutionAspectSupport(Executor defaultExecutor) {
        this(defaultExecutor, new SimpleTaskUncaughtExceptionHandler());
    }

    public TaskExecutionAspectSupport(Executor defaultExecutor, TaskUncaughtExceptionHandler exceptionHandler) {
        this.defaultExecutor = defaultExecutor;
        this.exceptionHandler = exceptionHandler;
    }

    public void setExecutor(Executor executor) {
        this.defaultExecutor = executor;
    }

    public void setExceptionHandler(TaskUncaughtExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * 获取异步任务的执行者
     */
    protected AsyncTaskExecutor determineTaskExecutor(Method method) {

        AsyncTaskExecutor executor = this.executors.get(method);

        if (executor == null) {
            Executor targetExecutor;

            String qualifier = getExecutorQualifier(method);

            if (StringUtils.hasLength(qualifier)) {
                targetExecutor = findQualifiedExecutor(this.beanFactory, qualifier);
            } else {
                targetExecutor = this.defaultExecutor;
                if (targetExecutor == null) {
                    synchronized (this.executors) {
                        if (this.defaultExecutor == null) {
                            this.defaultExecutor = getDefaultExecutor(this.beanFactory);
                        }
                        targetExecutor = this.defaultExecutor;
                    }
                }
            }

            if (targetExecutor == null) {
                return null;
            }

            if (targetExecutor instanceof AsyncListenableTaskExecutor) {
                executor = (AsyncListenableTaskExecutor) targetExecutor;
            } else {
                executor = new TaskExecutorAdapter(targetExecutor);
            }

            this.executors.put(method, executor);
        }

        return executor;
    }

    private Executor findQualifiedExecutor(BeanFactory beanFactory, String qualifier) {
        if (beanFactory == null) {
            throw new IllegalStateException("BeanFactory must be set on " + getClass().getSimpleName() + " to access qualified executor '" + qualifier + "'");
        }
        return BeanFactoryAnnotationUtils.qualifiedBeanOfType(beanFactory, Executor.class, qualifier);
    }

    /**
     * 获取异步任务使用的线程池名称
     */
    protected abstract String getExecutorQualifier(Method method);

    protected Executor getDefaultExecutor(BeanFactory beanFactory) {
        if (beanFactory != null) {
            try {
                return beanFactory.getBean(TaskExecutor.class);
            } catch (NoUniqueBeanDefinitionException ex) {
                try {
                    return beanFactory.getBean(DEFAULT_TASK_EXECUTOR_BEAN_NAME, Executor.class);
                } catch (NoSuchBeanDefinitionException ex2) {
                    if (logger.isInfoEnabled()) {
                        logger.info("More than one TaskExecutor bean found within the context, and none is named " +
                                "'taskExecutor'. Mark one of them as primary or name it 'taskExecutor' (possibly " +
                                "as an alias) in order to use it for async processing: " + ex.getBeanNamesFound());
                    }
                }
            } catch (NoSuchBeanDefinitionException ex) {
                logger.debug("Could not find default TaskExecutor bean", ex);
                logger.info("No TaskExecutor bean found for async processing");
            }
        }
        return null;
    }

}
