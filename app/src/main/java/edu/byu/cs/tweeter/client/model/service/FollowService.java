package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    /**
     * Observer used by the class above this one in the presenter so that we can communicate if
     * getFollowing succeeded or failed it's attempt to access the database.
     */
    public interface getFollowingObserver {
        void getFollowingSucceeded(List<User> users, boolean hasMorePages); // are there more users?
        void getFollowingFailed(String message);
        void getFollowingThrewException(Exception ex);
    }

    /**
     * Activates asynchronous event which gets a list of a given number of followers and the name of
     * the last followee. The current user can scroll through these and the final name will indicate
     * when it's time to get more names and also where to start.
     *
     * @param authToken Verify that the user has permissions
     * @param targetUser Search from this user's list of follows
     * @param numItemsToGet The number of followers to grab
     * @param lastFollowee The last follow grabbed on this method usage
     * @param observer The observer which will carry successes or failures interfaced above.
     */
    public void getFollowing(AuthToken authToken, User targetUser, int numItemsToGet,
                             User lastFollowee, getFollowingObserver observer) {

        GetFollowingTask getFollowingTask = new GetFollowingTask(authToken,
                targetUser, numItemsToGet, lastFollowee, new GetFollowingHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends Handler {

        private getFollowingObserver observer;

        public GetFollowingHandler(getFollowingObserver observer) {
            this.observer = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                observer.getFollowingSucceeded(followees, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                observer.getFollowingFailed(message);
            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.getFollowingThrewException(ex);
            }
        }
    }

}
