package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundTaskUtils {
    public static void runTask(Runnable Task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(Task);
    }
}
