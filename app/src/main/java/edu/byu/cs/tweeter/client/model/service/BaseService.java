package edu.byu.cs.tweeter.client.model.service;

import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;

public abstract class BaseService {

    BackgroundTask taskToExecute;
    BackgroundTaskHandler taskHandler;

    protected void setTaskHandler(BackgroundTaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

    /**
     * Will actually execute whatever service was inputted at the start.
     * @param taskToExecute
     */
    protected void executeService(BackgroundTask taskToExecute) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(taskToExecute);
    }



}
