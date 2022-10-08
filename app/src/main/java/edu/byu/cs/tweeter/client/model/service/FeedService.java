package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedService {
    public interface FeedObserver {
        void handleFeedSuccess(List<Status> statuses, boolean morePages);
        void handleFeedFailure(String message);
        void handleFeedThrewException(Exception ex);
    }
    public void getFeed(AuthToken token, User user, int limit, Status lastStatus, FeedObserver observer) {
        GetFeedTask getFeedTask = new GetFeedTask(token, user, limit, lastStatus, new FeedHandler(observer));
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(getFeedTask);
    }

    private class FeedHandler extends Handler {
        private FeedObserver observer;
        public FeedHandler(FeedObserver observer) {
            this.observer = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
                boolean morePages = (boolean) msg.getData().getSerializable(GetFeedTask.MORE_PAGES_KEY);
                observer.handleFeedSuccess(statuses, morePages);
            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
                observer.handleFeedFailure(message);
            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
                observer.handleFeedThrewException(ex);
            }
        }
    }
}
