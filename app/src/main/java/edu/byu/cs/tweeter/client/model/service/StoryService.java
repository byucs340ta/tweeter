package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryService {
    public interface StoryObserver {
        void handleStorySuccess(List<Status> statuses, boolean hasMorePages);
        void handleStoryFailure(String message);
        void handleStoryThrewException(Exception ex);
    }

    public void getStory(AuthToken token, User user, int limit, Status lastStatus, StoryObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(token, user, limit, lastStatus, new StoryHandler(observer));
    }

    private class StoryHandler extends Handler {
        private StoryObserver observer;

        public StoryHandler(StoryObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
                observer.handleStorySuccess(statuses, hasMorePages);
            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
                observer.handleStoryFailure(message);
            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                observer.handleStoryThrewException(ex);
            }
        }
    }
}
