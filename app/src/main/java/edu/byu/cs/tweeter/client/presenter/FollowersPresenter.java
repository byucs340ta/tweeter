package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends BasePresenter<FollowersPresenter.FollowersView> {

    // MARK - Class Variables
    private static final int PAGE_SIZE = 10;
    private FollowService followService;
    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading = false;

    // MARK - Interface Methods
    public interface FollowersView extends BasePresenter.View {
        void setLoadingFooter(boolean value);
        void addFollowers(List<User> followers);
        void getUserProfile(User user);
    }

    // MARK - Class constructor
    public FollowersPresenter(FollowersView view) {
        super(view);
        followService = new FollowService();
    }

    // MARK - Getter Methods
    public boolean isLoading() {
        return isLoading;
    }
    public boolean hasMorePages() {
        return hasMorePages;
    }

    // MARK - Methods
    public void getUserProfile(String userAlias) {
        userService.getUserProfile(Cache.getInstance().getCurrUserAuthToken(), userAlias, new FollowersPresenter.GetUserProfileObserver());
    }

    public void loadMoreItems(User user) {
        followService.loadMoreItemsFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new GetFollowersObserver());
        isLoading = true;
        view.setLoadingFooter(true);
    }

    // MARK - Inner Classes
    private class GetFollowersObserver extends BaseObserver implements PagedNotificationObserver<User> {
        public GetFollowersObserver() {
            super("get followers");
        }
        @Override
        public void handleSuccess(List<User> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastFollower = (items.size() > 0) ? items.get(items.size() - 1) : null;
            view.addFollowers(items);
            FollowersPresenter.this.hasMorePages = hasMorePages;
        }
        @Override
        public void processFailure() {
            isLoading = false;
            view.setLoadingFooter(false);
        }
    }

    // TODO: Duplicates of this exist
    private class GetUserProfileObserver extends BaseObserver implements GetUserObserver {

        public GetUserProfileObserver() {
            super("get user's profile");
        }

        @Override
        public void handleSuccess(User user) {
            view.getUserProfile(user);
        }
    }
}