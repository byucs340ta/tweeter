package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.presenter.presenter.views.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends BasePresenter implements GetUserObserver {
    private static final String LOG_TAG = "PagedPresenter";

    protected PagedView<T> view;
    protected static final int PAGE_SIZE = 10;
    protected T lastItem;
    protected boolean hasMorePages = true;
    protected boolean isLoading = false;

    public PagedPresenter(PagedView<T> view, AuthToken authToken, User targetUser) {
        super(authToken, targetUser);
        this.view = view; // this will always get overwritten in children, probably a better way to do this
    }

    public void loadMoreItems() {
        if (!isLoading && hasMorePages) {
            isLoading = true; // don't want to load again during this async method call
            view.setLoading(true); // to add message to view
            runGetService();
        }
    }

    protected abstract void runGetService();

    public void pagedSuccess(List<T> items, boolean hasMorePages) {
        lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
        this.hasMorePages = hasMorePages;

        view.setLoading(false);
        isLoading = false;
        view.addItems(items);
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

    @Override
    public void serviceFailure(String message) {
        view.displayErrorMessage(message);
        isLoading = false;
        view.setLoading(false);
    }

}
