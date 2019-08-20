package me.galaxy.sample;

import me.galaxy.manager.TaskInvokeHelper;
import me.galaxy.task.executor.TaskExecuteActor;
import me.galaxy.task.executor.TaskExecutorCenter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        TestService testService = context.getBean(TestService.class);
        testService.send("hello world");
        testService.send2();
        System.out.println("start");

//        TaskInvokeHelper helper = context.getBean(TaskInvokeHelper.class);
//        helper.invoke("guozhongdoubi", "hello world2");
//        System.out.println("start actor");

    }

}
