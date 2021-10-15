package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTask;

public abstract class BaseService {

    /**
     * Will actually execute whatever service was inputted at the start.
     * @param taskToExecute
     */
    protected void executeService(BackgroundTask taskToExecute) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(taskToExecute);
    }
}
