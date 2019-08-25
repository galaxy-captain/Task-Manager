package me.galaxy.task.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TaskResult<V> extends FutureTask<V> {

    public interface TaskResultListener<V> {

        void onSuccess(V result);

        void onFailure(Throwable ex);

    }

    private TaskResultListener<V> listener;

    public TaskResult(Callable<V> callable) {
        super(callable);
    }

    public TaskResult(Runnable runnable, V result) {
        super(runnable, result);
    }

    public void addListener(TaskResultListener<V> listener) {
        this.listener = listener;
    }

    @Override
    protected final void done() {

        if (this.listener == null) {
            return;
        }

        Throwable cause;
        try {
            V result = get();
            this.listener.onSuccess(result);
            return;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return;
        } catch (ExecutionException ex) {
            cause = ex.getCause();
            if (cause == null) {
                cause = ex;
            }
        } catch (Throwable ex) {
            cause = ex;
        }
        this.listener.onFailure(cause);
    }

}
