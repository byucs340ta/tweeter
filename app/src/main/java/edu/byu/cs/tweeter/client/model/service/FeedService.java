package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedService {

    /**
     * Feed Observer
     */
    public interface getFeedObserver {
        void getFeedSucceeded(List<Status>statuses, boolean hasMorePages); // What to pass back for a successful feed query?
        void getFeedFailed(String message);
        void getFeedThrewException(Exception ex);
    }

    public void getFeed(AuthToken authToken, User targetUser, int numItemsToGet,
                        Status lastStatus, FeedService.getFeedObserver observer) {

        GetFeedTask getFeedTask = new GetFeedTask(authToken, targetUser, numItemsToGet, lastStatus,
                new GetFeedHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends Handler {

        private getFeedObserver observer;

        public GetFeedHandler(getFeedObserver observer) { this.observer = observer; }

        @Override
        public void handleMessage(@NonNull Message msg) {
//            isLoading = false;
//            removeLoadingFooter();

            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
                observer.getFeedSucceeded(statuses, hasMorePages);
            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
                observer.getFeedFailed(message);
            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                observer.getFeedThrewException(ex);
            }
        }
    }

}
