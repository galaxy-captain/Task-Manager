package me.galaxy.task.status;

import me.galaxy.task.executor.AnnotationTaskExecuteActor;
import me.galaxy.task.executor.TaskExecuteActorVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-19 18:22
 **/
public class DefaultTaskLifeCycle implements TaskLifeCycle {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTaskLifeCycle.class);

    @Override
    public String onInitialize(TaskExecuteActorVisitor visitor, Object[] arguments) {
        return System.currentTimeMillis() + "";
    }

    @Override
    public void onWait(TaskExecuteActorVisitor visitor) {
        String msg = String.format("id=%s,status=%s", visitor.getId(), "onWait");
        System.out.println(msg);
    }

    @Override
    public void onRunning(TaskExecuteActorVisitor visitor) {
        String msg = String.format("id=%s,status=%s", visitor.getId(), "onRunning");
        System.out.println(msg);
    }

    @Override
    public void onAchieved(TaskExecuteActorVisitor visitor, Object result) {
        String msg = String.format("id=%s,status=%s", visitor.getId(), "onAchieved");
        System.out.println(msg);
    }

    @Override
    public void onBroken(TaskExecuteActorVisitor visitor, Throwable t, Object[] arguments) {
        String msg = String.format("id=%s,status=%s", visitor.getId(), "onBroken");
        System.out.println(msg);
        t.printStackTrace();
    }

}