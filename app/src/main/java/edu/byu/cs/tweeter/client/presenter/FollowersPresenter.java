package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {

    private static final int PAGE_SIZE = 10;
    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public interface View {
        void displayErrorMessage(String message);
        void setLoadingFooter(boolean b);
        void addFollowers(List<User> followees);
        void setIntent(User user);
    }

    private View view;
    private FollowService followService;
    private UserService userService;

    public FollowersPresenter(View view) {
        this.view = view;
        this.followService = new FollowService();
        this.userService = new UserService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new GetFollowersObserver());
        }
    }

    public void getUser(String alias) {
        userService.getUser(alias, new GetUserObserver());
    }

    public class GetFollowersObserver implements FollowService.GetFollowersObserver {

        @Override
        public void handleSuccess(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addFollowers(followers);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage("Failed to get followers: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage("Failed to get followers because of exception: " + exception.getMessage());
        }
    }

    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            view.setIntent(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get user's profile because of an exception: " + exception.getMessage());
        }
    }
}
