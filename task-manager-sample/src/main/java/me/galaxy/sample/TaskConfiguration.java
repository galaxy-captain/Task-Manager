package me.galaxy.sample;

import me.galaxy.manager.TaskInvokeHelper;
import me.galaxy.task.executor.TaskExecutorCenter;
import me.galaxy.task.status.TaskLifeCycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-08-20 18:22
 **/
@Configuration
public class TaskConfiguration {

    @Bean
    public TaskExecutorCenter taskExecutorCenter() {
        return new TaskExecutorCenter();
    }

    @Bean
    public TaskLifeCycle taskLifeCycle() {
        return new TaskRecordWithRDS();
    }

    @Bean(name = "taskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newFixedThreadPool(4));
    }

    @Bean
    public TaskInvokeHelper taskInvokeHelper(TaskExecutorCenter taskExecutorCenter,
                                             TaskLifeCycle taskLifeCycle,
                                             AsyncTaskExecutor asyncTaskExecutor) {

        TaskInvokeHelper helper = new TaskInvokeHelper(taskExecutorCenter);
        helper.setLifeCycle(taskLifeCycle);
        helper.setExecutor(asyncTaskExecutor);

        return helper;
    }

}