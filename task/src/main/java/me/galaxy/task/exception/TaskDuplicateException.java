package me.galaxy.task.exception;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-20 12:03
 **/
public class TaskDuplicateException extends RuntimeException {

    public TaskDuplicateException(String message) {
        super("Task name is duplicated: " + message);
    }

}