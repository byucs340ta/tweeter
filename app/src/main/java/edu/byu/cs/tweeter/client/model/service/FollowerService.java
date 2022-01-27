package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerService {

    private final GetFollowersObserver observer;

    public interface GetFollowersObserver {
        void handleSuccessFollower(List<User> users, boolean hasMorePages, User lastFollower);
        void handleFailureFollower(String message);
        void handleExceptionFollower(Exception exception);
    }



    public FollowerService(GetFollowersObserver observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.observer = observer;
    }

    public void getFollowers(AuthToken authToken, User targetUser, int limit, User lastFollowee) {
        GetFollowersTask getFollowersTask = getGetFollowerTask(authToken, targetUser, limit, lastFollowee);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public GetFollowersTask getGetFollowerTask(AuthToken authToken, User targetUser, int limit, User lastFollowee) {
        return new GetFollowersTask(authToken, targetUser, limit, lastFollowee,
                new GetFollowersHandler(Looper.getMainLooper(), observer));
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends Handler {

        private final GetFollowersObserver observer;

        public GetFollowersHandler(Looper looper, GetFollowersObserver observer) {
            super(looper);
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
                User lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
                observer.handleSuccessFollower(followers, hasMorePages, lastFollower);
            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.handleFailureFollower(message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                observer.handleExceptionFollower(ex);
            }
        }
    }

}