package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter implements FeedService.getFeedObserver {

    private FeedPresenter.View view;
    private static final int PAGE_SIZE = 10;
    private AuthToken authToken;
    private User targetUser;
    private Status lastStatus;
    private boolean hasMorePages = true;
    private boolean isLoading = false;



    /**
     * View observer
     */
    public interface View {
        void addItems(List<Status> statuses);
        void setLoading(boolean isLoading);
//        void navigateToStatus(Status status); // the presenter gives the view status info to display
        void displayErrorMessage(String message);
        void displayInfoMessage(String message);
    }

    /**
     * Feed presenter
     */
    public FeedPresenter(FeedPresenter.View view, AuthToken authToken, User targetUser) {
        this.view = view;
        this.authToken = authToken;
        this.targetUser = targetUser;
    }

    /**
     * Attempts to get more items when the view asks for them.
     */
    public void loadMoreItems() {
        if (!isLoading && hasMorePages) {
            isLoading = true; // don't want to load again during this async method call
            view.setLoading(true); // to add message to view
            new FeedService().getFeed(authToken, targetUser, PAGE_SIZE, lastStatus, this);
        }
    }

    @Override
    public void getFeedSucceeded(List<Status>statuses, boolean hasMorePages) {
        lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        this.hasMorePages = hasMorePages;

        view.setLoading(false);
        isLoading = false;
        view.addItems(statuses);
    }

    @Override
    public void getFeedFailed(String message) {
        view.displayErrorMessage("Failed to get feed: " + message);

        isLoading = false;
        view.setLoading(false);
    }

    @Override
    public void getFeedThrewException(Exception ex) {
        view.displayErrorMessage("Failed to get feed because of exception: " + ex.getMessage());

        isLoading = false;
        view.setLoading(false);
    }



}
