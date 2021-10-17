package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.net.Uri;
import android.text.Spanned;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter implements PagedObserver<Status>, GetUserObserver {

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
        void navigateToUser(User user); // the presenter gives the view status info to display
        void navigateToWebsite(String clickable);
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

    // This responds to when ANY observer fails.
    @Override
    public void serviceFailure(String message) {
        view.displayErrorMessage(message);
    }



    //********************************** Get Feed *********************************//
    ////////////////////////////////////////////////////////////////////////////////

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
    public void pagedSuccess(List<Status> statuses, boolean hasMorePages) {
        lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        this.hasMorePages = hasMorePages;

        view.setLoading(false);
        isLoading = false;
        view.addItems(statuses);
    }



    //********************************** Get Users *********************************//
    /////////////////////////////////////////////////////////////////////////////////

    public void evaluateClickedItem(String clickable) {
        if (clickable.contains("http")) {
            view.navigateToWebsite(clickable);
        } else {
            goToUser(clickable);
        }
    }

    /**
     * Navigate to user
     */
    public void goToUser(String alias) {
        view.displayInfoMessage("Getting user's profile...");
        new UserService().getUser(authToken, alias, this);
    }

    @Override
    public void GetUserSucceeded(User user) {
        view.navigateToUser(user);
    }

}
