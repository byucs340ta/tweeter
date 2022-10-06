package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.PagedNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T, K> extends BasePresenter<PagedPresenter.PagedView<T>>{

    protected static final int PAGE_SIZE = 10;
    protected T lastItem;
    protected K service;
    protected boolean hasMorePages;
    protected boolean isLoading = false;

    public PagedPresenter(PagedView<T> view, K service) {
        super(view);
        this.service = service;
    }

    public interface PagedView<T> extends BasePresenter.View {
        void setLoadingFooter(boolean value);
        void addItemsToPage(List<T> items);
        void getUserProfile(User user);
    }

    // MARK - Getter Methods
    public boolean hasMorePages() {
        return hasMorePages;
    }
    public boolean isLoading() {
        return isLoading;
    }

    // MARK - Class Methods
    public void getUserProfile(String userAlias) {
        userService.getUserProfile(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserProfileObserver());
    }

    public void loadMoreItems(User user) {
        loadMorePages(user);
        isLoading = true;
        view.setLoadingFooter(true);
    }

    public abstract void loadMorePages(User user);

    // MARK: - Inner Classes
    protected class PagedObserver extends BaseObserver implements PagedNotificationObserver<T> {

        public PagedObserver() {
            super("get page");
        }

        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;

            view.addItemsToPage((List<T>) items);
            PagedPresenter.this.hasMorePages = hasMorePages;
        }

        @Override
        public void processFailure() {
            isLoading = false;
            view.setLoadingFooter(false);
        }
    }

    // TODO: Duplicates of this exist
    private class GetUserProfileObserver extends BaseObserver implements GetUserObserver {

        public GetUserProfileObserver() {
            super("get user's profile");
        }

        @Override
        public void handleSuccess(User user) {
            view.getUserProfile(user);
        }
    }
}
