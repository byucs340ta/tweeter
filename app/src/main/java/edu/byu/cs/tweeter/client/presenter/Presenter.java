package edu.byu.cs.tweeter.client.presenter;


import edu.byu.cs.tweeter.client.model.service.AccountService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;

public class Presenter {

    protected BaseView view;
    protected UserService userService;
    protected AccountService accountService;

    public Presenter(BaseView view) {
        this.view = view;
        userService = new UserService();
        accountService = new AccountService();
    }

    public abstract class Observer implements ServiceObserver {
        @Override
        public void handleFailure(String message) {
            view.displayMessage(getBaseMessage() + ": " + message);
        }
        @Override
        public void handleException(Exception ex) {
            view.displayMessage(getBaseMessage() + " because of exception: " + ex.getMessage());
        }
        protected abstract String getBaseMessage();
    }
}
