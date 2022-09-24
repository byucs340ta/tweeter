package edu.byu.cs.tweeter.client.presenter;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {

    // MARK: - Class Variables
    private View view;
    private UserService service;

    // MARK: - Interfaces
    public interface View {
        void displayMessage(String message);
        void setRegisteringToast(boolean value);
        void postRegisterUser(User registeredUser);
    }

    // MARK: - Constructors
    public RegisterPresenter(View view) {
        this.view = view;
        service = new UserService();
    }

    // MARK: - Class Methods
    public void register(String firstName, String lastName, String alias,
                         String password, BitmapDrawable imageToUpload) {

        view.setRegisteringToast(true);
        service.registerUser(firstName, lastName, alias, password, imageToUpload, new GetRegisterObserver());
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
    private class GetRegisterObserver implements UserService.GetRegisterObserver {

        @Override
        public void displayErrorMessage(String message) {
            view.displayMessage("Failed to register: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to register because of exception: " + ex.getMessage());
        }

        @Override
        public void registerUser(User registeredUser) {
            view.postRegisterUser(registeredUser);
            view.setRegisteringToast(false);
            view.displayMessage("Hello "+ registeredUser.getName());
        }
    }
}
