package me.galaxy.task.core.exception;

public class NoTaskException extends RuntimeException {

    private String name;

    public NoTaskException() {

    }

    public NoTaskException(String name) {
        super(name);
        this.name = name;
    }

    public NoTaskException(String name, Throwable cause) {
        super(name, cause);
        this.name = name;
    }

    public NoTaskException(Throwable cause) {
        super(cause);
    }

    public NoTaskException(String name, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(name, cause, enableSuppression, writableStackTrace);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
