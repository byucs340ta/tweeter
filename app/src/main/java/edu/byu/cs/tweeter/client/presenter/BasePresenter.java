package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class BasePresenter<T extends BasePresenter.View> {

    protected T view;
    protected UserService userService;

    public BasePresenter(T view) {
        this.view = view;
        userService = new UserService();
    }

    public interface View {
        void displayMessage(String Message);

    }

    // MARK: - Inner Classes
    protected abstract class BaseObserver implements ServiceObserver {
        String messageType;

        public BaseObserver(String messageType) {
            processFailure();
            this.messageType = messageType;
        }

        @Override
        public void displayErrorMessage(String message) {
            processFailure();
            view.displayMessage("Failed to " + messageType + ": " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to " + messageType + " because of exception: " + ex.getMessage());
        }

        protected void processFailure() {
            // By default do nothing
        }
    }
}