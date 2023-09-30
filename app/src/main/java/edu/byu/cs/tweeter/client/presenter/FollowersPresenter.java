package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.FollowService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.view.main.followers.FollowersFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter implements UserService.GetUserObserver, FollowService.GetFollowersObserver {
    private static final int PAGE_SIZE = 10;

    @Override
    public void getUserSucceeded(User user) {
        view.openMainView(user);
    }

    @Override
    public void getUserFailed(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void getFollowersSucceeded(List<User> followers, boolean hasMorePages, User lastFollower) {
        this.hasMorePages = hasMorePages;
        this.lastFollower = lastFollower;
        this.isLoading = false;
        view.endingLoad();
        view.addItems(followers);
    }

    @Override
    public void getFollowersFailed(String message) {
        this.isLoading = false;
        view.endingLoad();
        view.showErrorMessage(message);
    }

    public interface View {
        void showInfoMessage(String message);
        void showErrorMessage(String message);

        void openMainView(User user);

        void startingLoad();
        void endingLoad();

        void addItems(List<User> followers);
    }

    private View view;
    private User user;
    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public boolean isLoading() {
        return isLoading;
    }
    public boolean getHasMorePages() {
        return hasMorePages;
    }

    public FollowersPresenter(View view, User user) {
        this.view = view;
        this.user = user;
    }

    public void getUser(AuthToken authToken, String alias) {
        var userService = new UserService();
        userService.getUser(authToken, alias, this);
        view.showInfoMessage("Getting user's profile...");
    }

    public void loadMoreItems() {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.startingLoad();

            var followService = new FollowService();
            followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, this);
        }
    }

}
