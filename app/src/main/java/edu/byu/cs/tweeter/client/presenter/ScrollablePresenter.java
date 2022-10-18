package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.ScrollableObserver;
import edu.byu.cs.tweeter.client.presenter.view.ScrollableView;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class ScrollablePresenter<T> extends Presenter {
    protected static final int PAGE_SIZE = 10;

    private T lastItem;
    private boolean morePages;
    private boolean isLoading = false;

    public ScrollablePresenter(ScrollableView<T> view) {
        super(view);
    }
    public ScrollableView<T> getScrollableView() {
        return (ScrollableView<T>) view;
    }
    public T getLastItem() {
        return lastItem;
    }
    public boolean morePages() {
        return morePages;
    }
    public void setMorePages(boolean morePages) {
        this.morePages = morePages;
    }
    public boolean isLoading() {
        return isLoading;
    }
    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            getScrollableView().setLoadingStatus(true);

            getItems(user);
        }
    }
    protected abstract void getItems(User user);
    public void goToUser(String userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserObserver());
        getScrollableView().displayMessage("Getting user's profile...");
    }

    public abstract class GetItemsObserver extends Observer implements ScrollableObserver<T> {
        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            getScrollableView().setLoadingStatus(false);
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setMorePages(hasMorePages);

            getScrollableView().addItems(items);
        }
        @Override
        public void handleFailure(String message) {
            isLoading = false;
            getScrollableView().setLoadingStatus(false);

            super.handleFailure(message);
        }
        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            getScrollableView().setLoadingStatus(false);

            super.handleException(ex);
        }
    }
    public class GetUserObserver extends Observer implements edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver {
        @Override
        public void handleSuccess(User user) {
            getScrollableView().goToUser(user);
        }
        @Override
        protected String getBaseMessage() {
            return "Failed to get user's profile";
        }
    }

}
