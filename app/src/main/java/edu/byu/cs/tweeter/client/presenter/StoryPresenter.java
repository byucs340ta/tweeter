package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter implements PagedObserver<Status>, GetUserObserver {

    private StoryPresenter.View view;
    private static final int PAGE_SIZE = 10;
    private AuthToken authToken;
    private User targetUser;
    private Status lastStatus;
    private boolean hasMorePages = true;
    private boolean isLoading = false;

    //********************************** Get Story *********************************/

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
     * StoryPresenter
     */
    public StoryPresenter(StoryPresenter.View view, AuthToken authToken, User targetUser) {
        this.view = view;
        this.authToken = authToken;
        this.targetUser = targetUser;
    }

    //******************************* Get User stuff ***********************************//
    /////////////////////////////////////////////////////////////////////////////////////

    public void evaluateClickedItem(String clickable) {
        if (clickable.contains("http")) {
            view.navigateToWebsite(clickable);
        } else {
            goToUser(clickable);
        }
    }

    public void goToUser(String alias) {
        view.displayInfoMessage("Getting user's profile...");
        new UserService().getUser(authToken, alias, this);
    }

    @Override
    public void GetUserSucceeded(User user) {
        view.navigateToUser(user);
    }



    //***************************** Story Stuff ***************************************//
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Attempts to get more items when the view asks for them.
     */
    public void loadMoreItems() {
        if (!isLoading && hasMorePages) {
            isLoading = true; // don't want to load again during this async method call
            view.setLoading(true); // to add message to view
            new StoryService().getStory(authToken, targetUser, PAGE_SIZE, lastStatus, this);
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

    @Override
    public void serviceFailure(String message) {
        view.displayErrorMessage(message);

        isLoading = false;
        view.setLoading(false);
    }
}
