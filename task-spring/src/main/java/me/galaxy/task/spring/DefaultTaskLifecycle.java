package me.galaxy.task.spring;

import me.galaxy.task.core.TaskLifecycle;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-08-26 13:02
 **/
public class DefaultTaskLifecycle implements TaskLifecycle {

    @Override
    public void onInitialize(Object[] arguments) {

    }

    @Override
    public void onWait() {

    }

    @Override
    public void onRunning() {

    }

    @Override
    public void onAchieved(Object result) {

    }

    @Override
    public void onBroken(Throwable ex) {

    }

}