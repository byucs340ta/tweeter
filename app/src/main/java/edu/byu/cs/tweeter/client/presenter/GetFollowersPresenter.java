package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersPresenter {
    private static final int PAGE_SIZE = 10;

    private View view;
    private FollowService followService;
    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public GetFollowersPresenter(View view) {
        this.view = view;
        followService = new FollowService();
    }

    public interface View {
        void setLoadingFooter(boolean isLoading);
        void displayMessage(String message);
        void addMoreItems(List<User> followees);
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(true);
            followService.loadMoreItems(user, PAGE_SIZE, lastFollower, new GetFollowersObserver());
        }
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    private class GetFollowersObserver implements FollowService.Observer {
        @Override
        public void displayMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(message);
        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastFollower = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addMoreItems(followees);
        }
    }
}
