package edu.byu.cs.tweeter.client.presenter;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends BasePresenter<RegisterPresenter.RegisterView> {

    // MARK: - Interfaces
    public interface RegisterView extends BasePresenter.View {
        void setRegisteringToast(boolean value);
        void postRegisterUser(User registeredUser);
    }

    // MARK: - Constructors
    public RegisterPresenter(RegisterView view) {
        super(view);
        userService = new UserService();
    }

    // MARK: - Class Methods
    public void register(String firstName, String lastName, String alias,
                         String password, BitmapDrawable imageToUpload) {

        view.setRegisteringToast(true);
        userService.registerUser(firstName, lastName, alias, password, imageToUpload, new GetRegisterObserver());
    }

    public void validateRegistration(String firstName, String lastName, String alias,
                                     String password, Drawable imageToUpload) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (imageToUpload == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    // MARK: - Inner Classes
    private class GetRegisterObserver extends BaseObserver implements AuthenticateNotificationObserver {

        public GetRegisterObserver() {
            super("register");
        }

        @Override
        public void handleSuccess(User authenticatedUser) {
            view.postRegisterUser(authenticatedUser);
            view.setRegisteringToast(false);
            view.displayMessage("Hello "+ authenticatedUser.getName());
        }
    }
}