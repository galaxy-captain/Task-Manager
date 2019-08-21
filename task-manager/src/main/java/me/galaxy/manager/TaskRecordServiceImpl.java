package me.galaxy.manager;

import me.galaxy.manager.json.JacksonProvider;
import me.galaxy.manager.json.JsonProvider;
import me.galaxy.task.status.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-21 13:49
 **/
@Service
public class TaskRecordServiceImpl implements TaskRecordService {

    @Autowired
    private TaskRecordDAO taskRecordDAO;

    private JsonProvider jsonProvider = new JacksonProvider();

    @Override
    public String initTaskExecuteStage(String taskName, Object[] arguments, int executeCount, int maxRetryTimes) {

        String argumentJSON = jsonProvider.toJsonString(arguments);
        String taskId = generateTaskId(taskName, argumentJSON);

        taskRecordDAO.saveTaskArguments(taskId, argumentJSON);
        taskRecordDAO.saveTaskExecuteStage(taskId, taskName, TaskStatus.INIT, executeCount, maxRetryTimes);

        return taskId;
    }

    @Override
    public int saveTaskExecuteStage(String taskId, String taskName, TaskStatus status, int executeCount, int maxRetryTimes) {

        taskRecordDAO.saveTaskExecuteStage(taskId, taskName, status, executeCount, maxRetryTimes);

        return 0;
    }

    @Override
    public int saveBrokenTask(String taskId, String taskName) {
        return 0;
    }

    @Override
    public int saveAchievedTask(String taskId, String taskName) {


        return 0;
    }

    private String generateTaskId(String taskName, String arguments) {
        try {
            return MD5Util.encode(taskName + arguments + System.currentTimeMillis());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}