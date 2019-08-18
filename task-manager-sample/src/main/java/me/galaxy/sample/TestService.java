package me.galaxy.sample;

import me.galaxy.task.EnableTask;
import me.galaxy.task.Task;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@EnableTask
@Service
public class TestService {

    @Task
    public Future<String> send(String msg) {

        try {
            Thread.sleep(3000);
            System.out.println(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new AsyncResult<String>("finish");
    }

}
