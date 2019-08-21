package me.galaxy.task.status;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-19 11:09
 **/
public enum TaskStatus {

    INIT("INIT", "0"),
    WAIT("WAIT", "100"),
    RUNNING("RUNNING", "200"),
    ACHIEVED("ACHIEVED", "300"),
    BROKEN("BROKEN", "400");

    private String value;

    private String code;

    TaskStatus(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}