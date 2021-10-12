package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;

public class MainPresenter implements UserService.LogoutObserver {

    public MainPresenter(View view) { this.view = view; }

    private View view;

    public interface View {
        void logout();
        // todo: all of the other features in main, etc...
        void displayErrorMessage(String message);
        void clearErrorMessage();
        void displayInfoMessage(String message);
        void clearInfoMessage();
    }

    //******************************* Logout *********************************

    public void logout() {
        view.clearErrorMessage();
        view.clearInfoMessage();
        new UserService().logout(this); // THIS IS AN OBSERVER WTF?!
    }

    @Override
    public void logoutSucceeded() {
        view.logout();
        view.clearErrorMessage();
        view.displayInfoMessage("Successfully logged out.");
    }

    @Override
    public void logoutFailed(String message) {
        view.displayErrorMessage("Failed to logout: " + message);
    }

    @Override
    public void logoutThrewException(Exception ex) {
        view.displayErrorMessage("Failed to logout because of exception: " + ex.getMessage());
    }
}
