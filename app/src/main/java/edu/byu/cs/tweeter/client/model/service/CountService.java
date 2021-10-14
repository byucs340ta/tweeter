package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class CountService {

    //******************************** Followers *********************************//
    public interface GetFollowersObserver {
        void getFollowerCountSucceeded(int count);
        void getFollowerCountFailed(String message);
        void getFollowerCountThrewException(Exception ex);
    }

    public void countFollowers(AuthToken authToken, User targetUser, GetFollowersObserver observer) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(new GetFollowersCountHandler(observer),
                Cache.getInstance().getCurrUserAuthToken(), targetUser);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followersCountTask);
    }

    // GetFollowersCountHandler

    private class GetFollowersCountHandler extends Handler {
        GetFollowersCountHandler(GetFollowersObserver observer) { this.observer = observer; }
        GetFollowersObserver observer;

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
                observer.getFollowerCountSucceeded(count);
//                followerCount.setText(getString(R.string.followerCount, String.valueOf(count)));
            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
                observer.getFollowerCountFailed(message);
//                Toast.makeText(MainActivity.this, "Failed to get followers count: " + message, Toast.LENGTH_LONG).show();
            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
                observer.getFollowerCountThrewException(ex);
//                Toast.makeText(MainActivity.this, "Failed to get followers count because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    //******************************** Following ********************************//
    public interface GetFollowingObserver {
        void getFollowingCountSucceeded(int count);
        void getFollowingCountFailed(String message);
        void getFollowingCountThrewException(Exception ex);
    }

    public void countFollowing(AuthToken authToken, User targetUser, GetFollowingObserver observer) {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(new GetFollowingCountHandler(observer),
                Cache.getInstance().getCurrUserAuthToken(), targetUser);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followingCountTask);
    }

    // GetFollowingCountHandler

    private class GetFollowingCountHandler extends Handler {
        GetFollowingCountHandler(GetFollowingObserver observer) { this.observer = observer; }
        GetFollowingObserver observer;

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
                observer.getFollowingCountSucceeded(count);
//                followeeCount.setText(getString(R.string.followeeCount, String.valueOf(count)));
            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
                observer.getFollowingCountFailed(message);
//                Toast.makeText(MainActivity.this, "Failed to get following count: " + message, Toast.LENGTH_LONG).show();
            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
                observer.getFollowingCountThrewException(ex);
//                Toast.makeText(MainActivity.this, "Failed to get following count because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
