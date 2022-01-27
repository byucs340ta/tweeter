package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedService {

    private static final int PAGE_SIZE = 10;

    public interface GetFeedObserver {
        void handleSuccessFeed(List<Status> statuses, boolean hasMorePages, Status lastStatus) throws MalformedURLException;
        void handleFailureFeed(String message);
        void handleExceptionFeed(Exception e);
    }

    public static void getFeed(GetFeedObserver observer, User user, Status lastStatus) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastStatus, new GetFeedHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private static class GetFeedHandler extends Handler {

        private GetFeedObserver observer;

        public GetFeedHandler(GetFeedObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);

                Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;

                try {
                    observer.handleSuccessFeed(statuses, hasMorePages, lastStatus);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
                observer.handleFailureFeed(message);
            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                observer.handleExceptionFeed(ex);
            }
        }
    }
}