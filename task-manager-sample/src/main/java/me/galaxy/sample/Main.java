package me.galaxy.sample;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        TestService testService = context.getBean(TestService.class);
        Future<String> future = testService.send("hello world");
        System.out.println("start");
        System.out.println(future.get());
        System.out.println("end");
    }

}
