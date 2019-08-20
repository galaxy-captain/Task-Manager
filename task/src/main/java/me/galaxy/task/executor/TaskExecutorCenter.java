package me.galaxy.task.executor;

import me.galaxy.task.status.DefaultTaskLifeCycle;
import me.galaxy.task.Task;
import me.galaxy.task.status.TaskLifeCycle;
import me.galaxy.task.exception.TaskDuplicateException;
import me.galaxy.task.utils.FormatUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-20 10:37
 **/
public class TaskExecutorCenter {

    public static final AsyncTaskExecutor DEFAULT_EXECUTOR = new SimpleAsyncTaskExecutor();

    public static final TaskLifeCycle DEFAULT_LIFECYCLE = new DefaultTaskLifeCycle();

    private Map<String, TaskExecuteConfig> taskExecuteConfigs = new HashMap<>();

    /**
     * 注册任务
     */
    public void registerTask(Object clazz) {

        Map<Method, Task> taskAnnotationMethods = taskAnnotatedMethodLookup(clazz.getClass());

        if (taskAnnotationMethods.isEmpty()) {
            return;
        }

        for (Map.Entry<Method, Task> entry : taskAnnotationMethods.entrySet()) {

            TaskExecuteConfig config = buildTaskExecuteConfig(clazz, entry.getKey(), entry.getValue());

            if (this.taskExecuteConfigs.containsKey(config.getName())) {
                throw new TaskDuplicateException(config.getName());
            }

            this.taskExecuteConfigs.put(config.getName(), config);

        }

    }

    /**
     * 构建任务
     */
    private TaskExecuteConfig buildTaskExecuteConfig(Object clazz, Method method, Task task) {

        Class<?> targetClass = AopUtils.getTargetClass(clazz);
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        final Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        String taskUniqueName = generateTaskUniqueName(clazz, method, task);
        TaskExecuteConfig config = new TaskExecuteConfig();
        config.setName(taskUniqueName);
        config.setClazz(clazz);
        config.setMethod(userDeclaredMethod);
        config.setMaxRetryCount(task.retryTimes());

        return config;
    }

    public TaskExecuteConfig getTaskExecuteConfig(String name) {
        return taskExecuteConfigs.get(name);
    }

    public TaskExecuteActor buildTaskExecuteActor(String name, AsyncTaskExecutor executor, TaskLifeCycle lifeCycle) {
        TaskExecuteConfig config = getTaskExecuteConfig(name);

        if (executor == null) {
            executor = DEFAULT_EXECUTOR;
        }

        if (lifeCycle == null) {
            lifeCycle = DEFAULT_LIFECYCLE;
        }

        AnnotationTaskExecuteActor actor = new AnnotationTaskExecuteActor(config);
        actor.setExecutor(executor);
        actor.setLifeCycle(lifeCycle);

        return actor;
    }

    public TaskExecuteActor buildTaskExecuteActor(String name, AsyncTaskExecutor executor) {
        return buildTaskExecuteActor(name, executor, null);
    }

    public TaskExecuteActor buildTaskExecuteActor(String name, TaskLifeCycle lifeCycle) {
        return buildTaskExecuteActor(name, null, lifeCycle);
    }

    public TaskExecuteActor buildTaskExecuteActor(String name) {
        return buildTaskExecuteActor(name, null, null);
    }

    /**
     * 获取任务名称
     */
    public static String generateTaskUniqueName(Object clazz, Method method, Task task) {
        return FormatUtils.taskUniqueName(clazz, method, task);
    }

    private static Map<Method, Task> taskAnnotatedMethodLookup(Class<?> beanClass) {

        return MethodIntrospector.selectMethods(beanClass, new MethodIntrospector.MetadataLookup<Task>() {
            @Override
            public Task inspect(Method method) {
                return method.getAnnotation(Task.class);
            }
        });

    }

}