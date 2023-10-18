package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.StoryService;
import edu.byu.cs.tweeter.client.model.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter implements UserService.GetUserObserver, StoryService.GetStoryObserver {
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
    public void getStorySucceeded(List<Status> statuses, boolean hasMorePages, Status lastStatus) {
        this.lastStatus = lastStatus;
        this.hasMorePages = hasMorePages;
        this.isLoading = false;
        view.endingLoad();
        view.addItems(statuses);
    }

    @Override
    public void getStoryFailed(String message) {
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

        void addItems(List<Status> statuses);
    }

    private final View view;
    private final User user;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public boolean getHasMorePages() {
        return hasMorePages;
    }

    public boolean getIsLoading() {
        return isLoading;
    }

    public StoryPresenter(View view, User user) {
        this.view = view;
        this.user = user;
    }

    public void getUser(AuthToken authToken, String alias) {
        var userService = new UserService();
        userService.getUser(authToken, alias, this);
        view.showInfoMessage("Getting user's profile...");
    }

    public void loadMoreItems() {
        if (!isLoading) {
            isLoading = true;
            view.startingLoad();

            var storyService = new StoryService();
            storyService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, this);
        }
    }
}
