package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends BasePresenter<FollowingPresenter.FollowingView> {

    // MARK - Class Variables
    private static final int PAGE_SIZE = 10;
    private FollowService followService;
    private User lastFollowee;
    private boolean hasMorePages;
    private boolean isLoading = false;

    // MARK - Interface Methods
    public interface FollowingView extends BasePresenter.View {
        void setLoadingFooter(boolean value);
        void addFollowees(List<User> followees);
        void getUserProfile(User user);
    }

    // MARK - Class constructor
    public FollowingPresenter(FollowingView view) {
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
        userService.getUserProfile(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserProfileObserver());
    }

    public void loadMoreItems(User user) {
        followService.loadMoreItemsFollowees(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollowee, new GetFollowingObserver());
        isLoading = true;
        view.setLoadingFooter(true);
    }

    // MARK - Inner Classes
    private class GetFollowingObserver extends BaseObserver implements PagedNotificationObserver<User> {
        public GetFollowingObserver() {
            super("get following");
        }

        @Override
        public void handleSuccess(List<User> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastFollowee = (items.size() > 0) ? items.get(items.size() - 1) : null;
            view.addFollowees(items);
            FollowingPresenter.this.hasMorePages = hasMorePages;
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
