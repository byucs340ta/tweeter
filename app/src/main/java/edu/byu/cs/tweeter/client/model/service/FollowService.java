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
import edu.byu.cs.tweeter.client.model.service.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends BaseService {

    //******************************** Get Following *************************************

        public void getFollowing(AuthToken authToken, User targetUser, int numItemsToGet,
                             User lastFollowee, PagedObserver<User> observer) {
        GetFollowingTask getFollowingTask  = new GetFollowingTask(new PagedTaskHandler<User>(observer) {
        }, authToken, targetUser, numItemsToGet, lastFollowee);
        super.executeService(getFollowingTask);
    }



    //************************* Get Followers *****************************//

    public void getFollowers(AuthToken authToken, User targetUser, int numItemsToGet,
                             User lastFollower, PagedObserver<User> observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(new PagedTaskHandler<User>(observer) {
        }, authToken, targetUser, numItemsToGet, lastFollower);
        super.executeService(getFollowersTask);
    }



    //******************************** Add Following *************************************
    //todo: M2B -- Eliminate duplicate code by adding classes and using more inheritance

    public interface addFollowerObserver {
        void AddFollowersSucceeded(); // are there more users?
        void AddFollowersFailed(String message);
        void AddFollowersThrewException(Exception ex);
    }

    public void addFollower(AuthToken authToken, User selectedUser, FollowService.addFollowerObserver observer) {
        FollowTask followTask = new FollowTask(new FollowService.FollowHandler(observer), Cache.getInstance().getCurrUserAuthToken(),
                selectedUser);
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
    //todo: M2B -- Eliminate duplicate code by adding classes and using more inheritance

    public interface removeFollowerObserver {
        void RemoveFollowersSucceeded(); // are there more users?
        void RemoveFollowersFailed(String message);
        void RemoveFollowersThrewException(Exception ex);
    }

    public void removeFollower(AuthToken authToken, User selectedUser, FollowService.removeFollowerObserver observer) {
        FollowTask followTask = new FollowTask(new FollowService.UnfollowHandler(observer), authToken, selectedUser);
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



    //************************** is Follower *******************************/
    //todo: M2B -- Eliminate duplicate code by adding classes and using more inheritance

    public interface isFollowerObserver {
        void IsFollowerSucceeded(boolean isFollower); // are there more users?
        void IsFollowerFailed(String message);
        void IsFollowerThrewException(Exception ex);
    }

    public void isFollower(AuthToken authToken, User selectedUser, isFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(new IsFollowerHandler(observer),
                Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(),
                selectedUser);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    private class IsFollowerHandler extends Handler {
        IsFollowerHandler(isFollowerObserver observer) { this.observer = observer; }
        isFollowerObserver observer;

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
                observer.IsFollowerSucceeded(isFollower);
                // If logged in user if a follower of the selected user, display the follow button as "following"
            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                observer.IsFollowerFailed(message);
//                Toast.makeText(MainActivity.this, "Failed to determine following relationship: " + message, Toast.LENGTH_LONG).show();
            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                observer.IsFollowerThrewException(ex);
//                Toast.makeText(MainActivity.this, "Failed to determine following relationship because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


}
