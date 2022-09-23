package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {

    // MARK: - Class Variables

    private static final int PAGE_SIZE = 10;
    private StatusService service;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private View view;

    public interface View {
        void displayMessage(String message);
        void setLoadingFooter(boolean value);
        void addStatusesToStory(List<Status> statuses);
    }

    public StoryPresenter(View view) {
        service = new StatusService();
        this.view = view;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void loadMoreItems(User user) {
        isLoading = true;
        view.setLoadingFooter(true);
        service.loadMoreItemsStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetStoryObserver());
    }


    // MARK: - Inner Classes
    private class GetStoryObserver implements StatusService.GetStoryObserver {

        @Override
        public void addStatusToStory(List<Status> statuses, boolean hasMorePages) {
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            isLoading = false;
            view.setLoadingFooter(false);
            view.addStatusesToStory(statuses);
        }

        @Override
        public void displayErrorMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get story: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get story because of exception: " + ex.getMessage());
        }
    }
}