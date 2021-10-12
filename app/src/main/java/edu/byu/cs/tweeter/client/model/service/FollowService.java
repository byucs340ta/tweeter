package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    //******************************** Get Following *************************************

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

    //************************* Get Followers *****************************//

    /**
     * Observer used by the class above this one in the presenter so that we can communicate if
     * getFollowers succeeded or failed it's attempt to access the database.
     */
    public interface getFollowersObserver {
        void getFollowersSucceeded(List<User> users, boolean hasMorePages); // are there more users?
        void getFollowersFailed(String message);
        void getFollowersThrewException(Exception ex);
    }

    public void getFollowers(AuthToken authToken, User targetUser, int numItemsToGet,
                             User lastFollower, getFollowersObserver observer) {

        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken,
                targetUser, numItemsToGet, lastFollower, new GetFollowersHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends Handler {

        private getFollowersObserver observer;

        public GetFollowersHandler(getFollowersObserver observer) { this.observer = observer; }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
                observer.getFollowersSucceeded(followers, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.getFollowersFailed(message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                observer.getFollowersThrewException(ex);
            }
        }
    }



    //******************************** Add Following *************************************

    public interface addFollowerObserver {
        void AddFollowersSucceeded(); // are there more users?
        void AddFollowersFailed(String message);
        void AddFollowersThrewException(Exception ex);
    }

    public void addFollower(AuthToken authToken, User selectedUser, FollowService.addFollowerObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowService.FollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    private class FollowHandler extends Handler {
        FollowHandler(FollowService.addFollowerObserver observer) { this.observer = observer; }
        FollowService.addFollowerObserver observer;

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                observer.AddFollowersSucceeded();
            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
                observer.AddFollowersFailed(message);
            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
                observer.AddFollowersThrewException(ex);
            }
        }
    }


    //******************************** Remove Following *************************************//

    public interface removeFollowerObserver {
        void RemoveFollowersSucceeded(); // are there more users?
        void RemoveFollowersFailed(String message);
        void RemoveFollowersThrewException(Exception ex);
    }

    public void removeFollower(AuthToken authToken, User selectedUser, FollowService.removeFollowerObserver observer) {
        FollowTask followTask = new FollowTask(authToken, selectedUser, new FollowService.UnfollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    private class UnfollowHandler extends Handler {
        UnfollowHandler(FollowService.removeFollowerObserver observer) {
            this.observer = observer;
        }
        FollowService.removeFollowerObserver observer;

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                observer.RemoveFollowersSucceeded();
            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
                observer.RemoveFollowersFailed(message);
            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
                observer.RemoveFollowersThrewException(ex);
           }
        }
    }

}
