package me.galaxy.sample;

import me.galaxy.manager.TaskInvokeHelper;
import me.galaxy.task.executor.TaskExecuteActor;
import me.galaxy.task.executor.TaskExecutorCenter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        TestService testService = context.getBean(TestService.class);
        ListenableFuture<String> future1 = testService.send("hello world - 1");
        System.out.println("start");
       future1.addCallback(new ListenableFutureCallback<String>() {
           @Override
           public void onFailure(Throwable ex) {
                ex.printStackTrace();
           }

           @Override
           public void onSuccess(String result) {
               System.out.println(result);
           }
       });

        TaskInvokeHelper helper = context.getBean(TaskInvokeHelper.class);
        Future<String> future2 = helper.invoke("send", "hello world - 2");
        System.out.println("start");
        System.out.println(future2.get());

    }

}
