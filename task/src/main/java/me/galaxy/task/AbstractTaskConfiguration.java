package me.galaxy.task;


import me.galaxy.task.aop.TaskUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.Executor;

@Configuration
public abstract class AbstractTaskConfiguration implements ImportAware {

    protected AnnotationAttributes enableTask;

    protected Executor executor;

    protected TaskUncaughtExceptionHandler uncaughtExceptionHandler;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableTask = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableTask.class.getName(), false));
        if (this.enableTask == null) {
            throw new IllegalArgumentException("@EnableTask is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Autowired(required = false)
    public void setTaskConfigurers(Collection<TaskConfigurer> configurers) {

        if (CollectionUtils.isEmpty(configurers)) {
            return;
        }

        if (configurers.size() > 1) {
            throw new IllegalStateException("Only one TaskConfigurer may exist");
        }

        TaskConfigurer configurer = configurers.iterator().next();

        this.executor = configurer.getAsyncExecutor();
        this.uncaughtExceptionHandler = configurer.getTaskUncaughtExceptionHandler();
    }

}
