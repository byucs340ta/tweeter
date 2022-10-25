package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.ServiceObserver;

public abstract class BasePresenter<T extends BasePresenter.View> {

    protected T view;
    protected UserService userService;
    private FollowService followService;
    private StatusService statusService;

    public BasePresenter(T view) {
        this.view = view;

        // removed in mockito video example
        this.userService = getUserService();
        this.followService = getFollowService();
        this.statusService = getStatusService();
    }

    protected UserService getUserService() {
        if(userService == null){
            userService = new UserService();
        }
        return userService;
    }

    protected FollowService getFollowService() {
        if(followService == null) {
            followService = new FollowService();
        }
        return followService;
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public interface View {
        void displayMessage(String Message);
    }

    // MARK: - Inner Classes
    protected abstract class BaseObserver implements ServiceObserver {
        String messageType;

        public BaseObserver(String messageType) {
            this.messageType = messageType;
        }

        @Override
        public void displayErrorMessage(String message) {
            processFailure();
            view.displayMessage("Failed to " + messageType + ": " + message);
        }

        @Override
        public void displayException(Exception ex) {
            processFailure();
            view.displayMessage("Failed to " + messageType + " because of exception: " + ex.getMessage());
        }

        protected void processFailure() {
            // By default do nothing
        }
    }
}