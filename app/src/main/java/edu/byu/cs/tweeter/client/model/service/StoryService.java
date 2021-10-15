package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryService {
    //todo: M2B -- Eliminate duplicate code by adding classes and using more inheritance

    /**
     * Story Observer
     */
    public interface getStoryObserver {
        void getStorySucceeded(List<Status> statuses, boolean hasMorePages); // What to pass back for a successful feed query?
        void getStoryFailed(String message);
        void getStoryThrewException(Exception ex);
    }

    public void getStory(AuthToken authToken, User targetUser, int numItemsToGet,
                        Status lastStatus, StoryService.getStoryObserver observer) {

        GetStoryTask getStoryTask = new GetStoryTask(new StoryService.GetStoryHandler(observer),
                authToken, targetUser, numItemsToGet, lastStatus);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends Handler {

        private StoryService.getStoryObserver observer;

        public GetStoryHandler(StoryService.getStoryObserver observer) { this.observer = observer; }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.ITEMS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);
                observer.getStorySucceeded(statuses, hasMorePages);
            } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
                observer.getStoryFailed(message);
            } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
                observer.getStoryThrewException(ex);
            }
        }
    }

}
