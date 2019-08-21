package me.galaxy.sample;

import me.galaxy.manager.TaskInvokeHelper;
import me.galaxy.task.executor.TaskExecutorCenter;
import me.galaxy.task.status.TaskLifeCycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-20 18:22
 **/
@Configuration
public class TaskConfiguration {

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/task_info?characterEncoding=utf8&useSSL=true");
        dataSource.setUsername("root");
        dataSource.setPassword("Aa761349852.");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DriverManagerDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

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