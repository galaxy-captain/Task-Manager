package me.galaxy.sample;

import me.galaxy.task.EnableTask;
import me.galaxy.task.Task;
import me.galaxy.task.TaskResult;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

@EnableTask
@Service
public class TestService {

    @Task(name = "send", retryTimes = 3)
    public ListenableFuture<String> send(String msg) {

        try {
            Thread.sleep(1000);
            System.out.println(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("错误");

//        return new TaskResult<String>("ok");
    }

    @Task(retryTimes = 1)
    public void send2() {
        try {
            Thread.sleep(1000);
            System.out.println("haha");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
