package me.galaxy.task.spring;

import me.galaxy.task.core.AbstractTaskCenter;
import me.galaxy.task.core.TaskExecuteActor;
import me.galaxy.task.core.TaskExecuteConfiguration;
import me.galaxy.task.core.TaskLifecycle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-08-26 12:07
 **/
public class SpringTaskCenter extends AbstractTaskCenter implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    protected Map<String, TaskExecuteConfiguration> buildTaskConfig(Object object) {

        Map<String, TaskExecuteConfiguration> map = new HashMap<String, TaskExecuteConfiguration>();




        return map;
    }

    @Override
    protected TaskExecuteActor buildTaskActor(TaskExecuteConfiguration config) {

        TaskLifecycle lifecycle = determineTaskLifecycle(config);
        AsyncTaskExecutor executor = determineTaskExecutor(config);

        return new SpringTaskExecuteActor(config, executor, lifecycle);
    }

    private AsyncTaskExecutor determineTaskExecutor(TaskExecuteConfiguration config) {

        String name = config.getExecutor();

        try {

            if (StringUtils.isEmpty(name)) {
                return beanFactory.getBean(AsyncTaskExecutor.class);
            }

            return beanFactory.getBean(name, AsyncTaskExecutor.class);

        } catch (NoSuchBeanDefinitionException e) {

            return new SimpleAsyncTaskExecutor();
        }
    }

    private TaskLifecycle determineTaskLifecycle(TaskExecuteConfiguration config) {

        String name = config.getLifecycle();

        try {

            if (StringUtils.isEmpty(name)) {
                return beanFactory.getBean(TaskLifecycle.class);
            }

            return beanFactory.getBean(name, TaskLifecycle.class);

        } catch (NoSuchBeanDefinitionException e) {

            return new DefaultTaskLifecycle();
        }

    }

}