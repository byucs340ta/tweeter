package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {

    // MARK - Class Variables
    private static final int PAGE_SIZE = 10;
    private View view;
    private StatusService service;
    private Status lastStatus;
    private boolean hasMoreStatuses;
    private boolean isLoading = false;

    // MARK - Interface Classes
    public interface View {
        void displayMessage(String message);
        void setLoadingFooter(boolean value);
        void addStatusToFeed(List<Status> statuses);
    }

    // MARK - Constructor
    public FeedPresenter(View view) {
        this.view = view;
        service = new StatusService();
    }

    // MARK - Getter Methods
    public boolean hasMorePages() {
        return hasMoreStatuses;
    }
    public boolean isLoading() {
        return isLoading;
    }

    // MARK - Class Methods
    public void loadMoreItems(User user) {
        isLoading = true;
        view.setLoadingFooter(true);
        service.loadMoreItemsFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetFeedObserver());
    }

    // MARK - Inner Classes
    private class GetFeedObserver implements StatusService.GetFeedObserver {
        @Override
        public void addToFeed(List<Status> statuses, boolean hasMoreStatuses) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addStatusToFeed(statuses);
            FeedPresenter.this.hasMoreStatuses = hasMoreStatuses;
        }

        @Override
        public void displayErrorMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get feed: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get feed because of exception: " + ex.getMessage());
        }
    }
}
