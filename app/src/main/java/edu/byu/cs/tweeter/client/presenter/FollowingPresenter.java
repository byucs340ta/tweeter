package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter implements FollowService.getFollowingObserver, UserService.GetUserObserver {

    /**
     * Way for this presenter to call the view back and give it responses to methods it called
     * An observer interface version of View for the presenter to reference
     */
    public interface View {
        void addItems(List<User> followees);
        void setLoading(boolean isLoading);
        void navigateToUser(User user); // the presenter gives the view user info to display
        void displayErrorMessage(String message);
        void displayInfoMessage(String message);
    }

    private static final String LOG_TAG = "FollowingPresenter";
    private static final int PAGE_SIZE = 10;

    private View view;
    private AuthToken authToken;
    private User targetUser;
    private User lastFollowee;
    private boolean hasMorePages = true;
    private boolean isLoading = false;

    /**
     * Construct the FollowingPresenter with necessary info from above class in View.
     * @param view
     * @param authToken
     * @param targetUser
     */
    public FollowingPresenter(View view, AuthToken authToken, User targetUser) {
        this.view = view;
        this.authToken = authToken;
        this.targetUser = targetUser;
    }

    //******************************** Get Followees *********************************/

    /**
     * Attempts to get more items when the view asks for them.
     */
    public void loadMoreItems() {
        if (!isLoading && hasMorePages) {
            isLoading = true; // don't want to load again during this async method call
            view.setLoading(true); // to add message to view
            new FollowService().getFollowing(authToken, targetUser, PAGE_SIZE, lastFollowee, this);
        }
    }

    /**
     * From FollowService.getFollowingObserver
     * @param users User to navigate view to
     * @param hasMorePages Are there more pages to share?
     */
    @Override
    public void getFollowingSucceeded(List<User> users, boolean hasMorePages) {
        lastFollowee = (users.size() > 0) ? users.get(users.size() - 1) : null;
        this.hasMorePages = hasMorePages;

        view.setLoading(false);
        isLoading = false;
        view.addItems(users);
    }

    /**
     * From FollowService.getFollowingObserver
     * @param message Message to display after error
     */
    @Override
    public void getFollowingFailed(String message) {
        view.displayErrorMessage("Login failed: " + message); // Consider Dr. Wilkerson's approach in his code

        isLoading = false;
        view.setLoading(false);
    }

    /**
     * From FollowService.getFollowingObserver
     * @param ex Exception message to display
     */
    @Override
    public void getFollowingThrewException(Exception ex) {
        view.displayErrorMessage("Login failed: " + ex.getMessage()); // Consider Dr. Wilkerson's approach in his code

        isLoading = false;
        view.setLoading(false);
    }

    //********************************** Get Users *********************************/

    /**
     * Navigate to user
     */
    public void goToUser(String alias) {
        view.displayInfoMessage("Getting user's profile...");
        new UserService().getUser(authToken, alias, this);
    }

    /**
     * From UserService.GetUserObserver interface
     * @param user User to navigate view to
     */
    @Override
    public void getUserSucceeded(User user) {
        view.navigateToUser(user);
    }

    /**
     * From UserService.GetUserObserver interface
     * @param message Message to display after error
     */
    @Override
    public void getUserFailed(String message) {
        view.displayErrorMessage("Login failed: " + message);
    }

    /**
     * From UserService.GetUserObserver interface
     * @param ex Exception message to display
     */
    @Override
    public void getUserThrewException(Exception ex) {
        view.displayErrorMessage("Login failed: " + ex.getMessage());
    }
}
