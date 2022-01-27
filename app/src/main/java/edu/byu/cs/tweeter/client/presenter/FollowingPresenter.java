package edu.byu.cs.tweeter.client.presenter;


import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowingService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter implements FollowingService.GetFollowingObserver, UserService.GetUserObserver {

    private static final String LOG_TAG = "FollowingPresenter";
    private static final int PAGE_SIZE = 10;

    private final View view;
    private final User targetUser;
    private final AuthToken authToken;

    private User lastFollowee;
    private boolean hasMorePages = true;
    private boolean isLoading = false;

    /**
     * Creates an instance.
     *
     * @param view      the view for which this class is the presenter.
     * @param targetUser      the user that is currently logged in.
     * @param authToken the auth token for the current session.
     */
    public FollowingPresenter(View view, User targetUser, AuthToken authToken) {
        this.view = view;
        this.targetUser = targetUser;
        this.authToken = authToken;
    }

    public interface View {
        void setLoading(boolean value);
        void navigateToUser(User user);
        void addItems(List<User> newUsers);
        void displayErrorMessage(String message);
        void clearErrorMessage();
        void displayInfoMessage(String message);
        void clearInfoMessage();
    }

    public View getView() {
        return view;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public User getLastFollowee() {
        return lastFollowee;
    }

    public void setLastFollowee(User lastFollowee) {
        this.lastFollowee = lastFollowee;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void loadMoreItems() {
        if (!isLoading && hasMorePages) {
            setLoading(true);
            view.setLoading(true);

            getFollowing(authToken, targetUser, PAGE_SIZE, lastFollowee);
        }
    }

    public void getTargetUser(String alias) {
        UserService.getUsers(Cache.getInstance().getCurrUserAuthToken(), alias, this);
    }

    public void goToUser(User alias) {
        view.navigateToUser(alias);
    }

    /**
     * Requests the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned for a previous request. This is an asynchronous
     * operation.
     *
     * @param authToken    the session auth token.
     * @param targetUser   the user for whom followees are being retrieved.
     * @param limit        the maximum number of followees to return.
     * @param lastFollowee the last followee returned in the previous request (can be null).
     */
    public void getFollowing(AuthToken authToken, User targetUser, int limit, User lastFollowee) {
        getFollowingService(this).getFollowing(authToken, targetUser, limit, lastFollowee);
    }

    /**
     * Returns an instance of {@link FollowingService}. Allows mocking of the FollowService class
     * for testing purposes. All usages of FollowService should get their FollowService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    public FollowingService getFollowingService(FollowingService.GetFollowingObserver observer) {
        return new FollowingService(observer);
    }

    @Override
    public void handleSuccess(List<User> users, boolean hasMorePages) {
        setLastFollowee((users.size() > 0) ? users.get(users.size() - 1) : null);
        setHasMorePages(hasMorePages);
        view.setLoading(false);
        view.addItems(users);
        setLoading(false);
    }

    @Override
    public void handleFailure(String message) {
        String errorMessage = "Failed to retrieve followees: " + message;
        Log.e(LOG_TAG, errorMessage);
        view.setLoading(false);
        view.displayErrorMessage(errorMessage);
        setLoading(false);
    }

    @Override
    public void handleException(Exception exception) {}

    @Override
    public void handleExceptionUser(Exception exception) {
        String errorMessage = "Failed to retrieve followees because of exception: " + exception.getMessage();
        Log.e(LOG_TAG, errorMessage, exception);

        view.setLoading(false);
        view.displayErrorMessage(errorMessage);
        setLoading(false);
    }

    @Override
    public void handleSuccessUser(User user) {
        view.navigateToUser(user);
    }

    @Override
    public void handleFailureUser(String message) {

    }
}