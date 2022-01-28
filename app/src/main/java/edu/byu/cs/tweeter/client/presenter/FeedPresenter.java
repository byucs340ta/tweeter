package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {

    private static final int PAGE_SIZE = 10;
    private Status lastStatus;

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
        void addFeed(List<Status> statuses);
        void setIntent(User user);
    }

    private View view;
    private StatusService statusService;
    private UserService userService;

    public FeedPresenter(View view) {
        this.view = view;
        this.statusService = new StatusService();
        this.userService = new UserService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetFeedObserver());
        }
    }

    public void getUser(String alias) {
        userService.getUser(alias, new GetUserObserver());
    }

    public class GetFeedObserver implements StatusService.GetFeedObserver {

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addFeed(statuses);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage("Failed to get feed: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayErrorMessage("Failed to get feed because of exception: " + exception.getMessage());
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
