package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter implements StoryService.GetStoryObserver, UserService.GetUserObserver {
    private final StoryPresenter.View view;
    private boolean isLoading = false;
    private boolean hasMorePages = true;
    private final User user;
    private Status lastStatus = null;

    public interface View {
        void navigateToUser(User user);
        void setLoading(boolean value);
        void displayMessage(String message);
        void addItems(List<Status> statuses);
    }

    public StoryPresenter(View view, User user) {
        this.view = view;
        this.user = user;
    }

    public void loadMoreItems() {
        if (!isLoading && hasMorePages) {
            isLoading = true;
            view.setLoading(true);

            new StoryService().getStory(this, user, lastStatus);
        }
    }

    public void getUsers(String alias) {
        UserService.getUsers(Cache.getInstance().getCurrUserAuthToken(), alias, this);
    }

    @Override
    public void handleSuccessStory(List<Status> statuses, boolean hasMorePages, Status lastStatus) {
        view.setLoading(false);
        view.addItems(statuses);
        this.hasMorePages = hasMorePages;
        isLoading = false;
    }

    @Override
    public void handleFailureStory(String message) {
        view.displayMessage("Failed to get story: " + message);
    }

    @Override
    public void handleExceptionStory(Exception e) {
        view.displayMessage("Failed to get story because of exception: " + e.getMessage());
    }

    @Override
    public void handleSuccessUser(User user) {
        view.navigateToUser(user);
    }

    @Override
    public void handleFailureUser(String message) {

    }

    @Override
    public void handleExceptionUser(Exception exception) {

    }
}
