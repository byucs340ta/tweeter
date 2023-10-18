package edu.byu.cs.tweeter.client.model.services;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    public interface GetFollowingObserver {
        void getFollowingSucceeded(List<User> followees, boolean hasMorePages, User lastFollowee);

        void getFollowingFailed(String message);
    }

    public void getFollowing(AuthToken authToken, User user, int pageSize, User lastFollowee, GetFollowingObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(authToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public interface GetFollowersObserver {
        void getFollowersSucceeded(List<User> followers, boolean hasMorePages, User lastFollower);

        void getFollowersFailed(String message);
    }

    public void getFollowers(AuthToken authToken, User user, int pageSize, User lastFollower, GetFollowersObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, user, pageSize, lastFollower, new GetFollowersHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }


    private static class GetFollowingHandler extends Handler {

        private final GetFollowingObserver observer;

        public GetFollowingHandler(GetFollowingObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                User lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
                observer.getFollowingSucceeded(followees, hasMorePages, lastFollowee);
            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                observer.getFollowingFailed("Failed to get following: " + message);
            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.getFollowingFailed("Failed to get following because of exception: " + ex.getMessage());
            }
        }
    }

    private static class GetFollowersHandler extends Handler {
        private final GetFollowersObserver observer;

        public GetFollowersHandler(GetFollowersObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }


        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);

                assert followers != null;
                User lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;

                observer.getFollowersSucceeded(followers, hasMorePages, lastFollower);
            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.getFollowersFailed("Failed to get followers: " + message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                assert ex != null;
                observer.getFollowersFailed("Failed to get followers because of exception: " + ex.getMessage());
            }
        }
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser, GetFollowersCountObserver followersObserver, GetFollowingCountObserver followingObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(followersObserver));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(followingObserver));
        executor.execute(followingCountTask);
    }

    public interface GetFollowersCountObserver {
        void getFollowersCountSucceeded(int count);

        void getFollowersCountFailed(String message);
    }

    private static class GetFollowersCountHandler extends Handler {

        private final GetFollowersCountObserver observer;

        public GetFollowersCountHandler(GetFollowersCountObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
//                followerCount.setText(getString(R.string.followerCount, String.valueOf(count)));
                observer.getFollowersCountSucceeded(count);
            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
//                Toast.makeText(MainActivity.this, "Failed to get followers count: " + message, Toast.LENGTH_LONG).show();
                observer.getFollowersCountFailed("Failed to get followers count: " + message);
            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
//                Toast.makeText(MainActivity.this, "Failed to get followers count because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                observer.getFollowersCountFailed("Failed to get followers count because of exception: " + ex.getMessage());
            }
        }
    }

    public interface GetFollowingCountObserver {
        void getFollowingCountSucceeded(int count);

        void getFollowingCountFailed(String message);
    }

    private static class GetFollowingCountHandler extends Handler {

        private final GetFollowingCountObserver observer;

        public GetFollowingCountHandler(GetFollowingCountObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
//                followeeCount.setText(getString(R.string.followeeCount, String.valueOf(count)));
                observer.getFollowingCountSucceeded(count);
            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
//                Toast.makeText(MainActivity.this, "Failed to get following count: " + message, Toast.LENGTH_LONG).show();
                observer.getFollowingCountFailed("Failed to get following count: " + message);
            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
//                Toast.makeText(MainActivity.this, "Failed to get following count because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                observer.getFollowingCountFailed("Failed to get following count because of exception: " + ex.getMessage());
            }
        }
    }

    public void isFollower(User selectedUser, IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public interface IsFollowerObserver {
        void isFollowerSucceeded(boolean isFollower);

        void isFollowerFailed(String message);
    }

    private static class IsFollowerHandler extends Handler {

        private final IsFollowerObserver observer;

        public IsFollowerHandler(IsFollowerObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
                observer.isFollowerSucceeded(isFollower);
            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                observer.isFollowerFailed("Failed to determine following relationship: " + message);
            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                observer.isFollowerFailed("Failed to determine following relationship because of exception: " + ex.getMessage());
            }
        }
    }

    public void follow(User selectedUser, FollowObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    public interface FollowObserver {
        void followSucceeded();
        void followFailed(String message);
    }

    private static class FollowHandler extends Handler {

        private final FollowObserver observer;

        public FollowHandler(FollowObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
//                updateSelectedUserFollowingAndFollowers();
//                updateFollowButton(false);
                observer.followSucceeded();
            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
//                Toast.makeText(MainActivity.this, "Failed to follow: " + message, Toast.LENGTH_LONG).show();
                observer.followFailed("Failed to follow: " + message);
            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
//                Toast.makeText(MainActivity.this, "Failed to follow because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                assert ex != null;
                observer.followFailed("Failed to follow because of exception: " + ex.getMessage());
            }

//            followButton.setEnabled(true);
        }
    }

    public void unfollow(User selectedUser, UnfollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }

    public interface UnfollowObserver {
        void unfollowSucceeded();
        void unfollowFailed(String message);
    }

    private static class UnfollowHandler extends Handler {
        private final UnfollowObserver observer;

        public UnfollowHandler(UnfollowObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                observer.unfollowSucceeded();
            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
                observer.unfollowFailed("Failed to unfollow: " + message);
            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
                assert ex != null;
                observer.unfollowFailed("Failed to unfollow because of exception: " + ex.getMessage());
            }
        }

    }
}
