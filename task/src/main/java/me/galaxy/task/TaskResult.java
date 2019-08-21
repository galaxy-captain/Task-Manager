package me.galaxy.task;

import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-08-21 11:58
 **/
public class TaskResult<V> implements ListenableFuture<V> {

    private final V value;

    private final ExecutionException executionException;


    /**
     * Create a new AsyncResult holder.
     *
     * @param value the value to pass through
     */
    public TaskResult(V value) {
        this(value, null);
    }

    /**
     * Create a new AsyncResult holder.
     *
     * @param value the value to pass through
     */
    private TaskResult(V value, ExecutionException ex) {
        this.value = value;
        this.executionException = ex;
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public V get() throws ExecutionException {
        if (this.executionException != null) {
            throw this.executionException;
        }
        return this.value;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws ExecutionException {
        return get();
    }

    @Override
    public void addCallback(ListenableFutureCallback<? super V> callback) {
        addCallback(callback, callback);
    }

    @Override
    public void addCallback(SuccessCallback<? super V> successCallback, FailureCallback failureCallback) {
        try {
            if (this.executionException != null) {
                Throwable cause = this.executionException.getCause();
                failureCallback.onFailure(cause != null ? cause : this.executionException);
            } else {
                successCallback.onSuccess(this.value);
            }
        } catch (Throwable ex) {
            // Ignore
        }
    }


    /**
     * Create a new async result which exposes the given value from {@link Future#get()}.
     *
     * @param value the value to expose
     * @see Future#get()
     * @since 4.2
     */
    public static <V> ListenableFuture<V> forValue(V value) {
        return new TaskResult<V>(value, null);
    }

    /**
     * Create a new async result which exposes the given exception as an
     * {@link ExecutionException} from {@link Future#get()}.
     *
     * @param ex the exception to expose (either an pre-built {@link ExecutionException}
     *           or a cause to be wrapped in an {@link ExecutionException})
     * @see ExecutionException
     * @since 4.2
     */
    public static <V> ListenableFuture<V> forExecutionException(Throwable ex) {
        return new TaskResult<V>(null, (ex instanceof ExecutionException ? (ExecutionException) ex : new ExecutionException(ex)));
    }

}