package me.galaxy.task;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.util.Assert;

@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ProxyTaskConfiguration extends AbstractTaskConfiguration {

    @Bean(name = "me.galaxy.task.internalTaskAnnotationProcessor")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TaskAnnotationBeanPostProcessor taskAnnotationBeanPostProcessor() {

        Assert.notNull(this.enableTask, "@EnableTask annotation metadata was not injected");

        TaskAnnotationBeanPostProcessor beanPostProcessor = new TaskAnnotationBeanPostProcessor();
        beanPostProcessor.setExecutor(this.executor);
        beanPostProcessor.setExceptionHandler(this.uncaughtExceptionHandler);
        beanPostProcessor.setOrder(this.enableTask.<Integer>getNumber("order"));

        return beanPostProcessor;
    }

}
