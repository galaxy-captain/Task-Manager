package me.galaxy.sample;

import me.galaxy.task.EnableTask;
import me.galaxy.task.Task;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@EnableTask
@Service
public class TestService {

    @Task(retryTimes = 0)
    public void send(String msg) {

        try {
            Thread.sleep(3000);
            System.out.println(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
