package me.galaxy.manager;

import me.galaxy.task.status.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-21 14:00
 **/
@Repository
public class TaskRecordDAO {

    public static final String INSERT_TASK_STAGE_SQL = "insert into task_execute_record(task_id, task_name, task_status, task_execute_count, task_execute_max, create_date, update_date) values(?,?,?,?,?,?,?)";

    public static final String INSERT_TASK_ARGUMENTS_SQL = "insert into task_argument_record(task_arguments, task_id, create_date, update_date) values(?,?,?,?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public int saveTaskExecuteStage(String taskId, String taskName, TaskStatus status, int executeCount, int maxRetryTimes) {

        Timestamp now = now();

        int result = jdbcTemplate.update(INSERT_TASK_STAGE_SQL, taskId, taskName, status.getValue(), executeCount, maxRetryTimes + 1, now, now);

        return result;
    }

    public int saveTaskArguments(String taskId, String arguments) {

        Timestamp now = now();

        int result = jdbcTemplate.update(INSERT_TASK_ARGUMENTS_SQL, arguments, taskId, now, now);

        return result;
    }

}