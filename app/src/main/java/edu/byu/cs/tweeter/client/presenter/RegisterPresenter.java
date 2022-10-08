package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.AccountService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter implements AccountService.RegisterObserver {
    private RegisterView view;
    public RegisterPresenter(RegisterView view) {
        this.view = view;
    }
    public  interface RegisterView {
        void displayErrorMessage(String message);
        void clearErrorMessage();

        void displayInfoMessage(String message);
        void clearInfoMessage();

        void navigateToUser(User user);
    }
    public void register(String firstName, String lastName, String alias, String password, String image) {
        String errorMessage = validateRegistration(firstName, lastName, alias, password, image);
        if (errorMessage == null) {
            view.clearInfoMessage();
            view.clearErrorMessage();
            view.displayInfoMessage("Registering...");
            new AccountService().register(firstName, lastName, alias, password, image, this);
        }
        else {
            view.displayErrorMessage(errorMessage);
        }
    }

    public String validateRegistration(String firstName, String lastName, String alias, String password, String imageToUpload) {
        if (firstName.length() == 0) {
            return "First Name cannot be empty.";
        }
        if (lastName.length() == 0) {
            return "Last Name cannot be empty.";
        }
        if (alias.length() == 0) {
            return "Alias cannot be empty.";
        }
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }
        if (imageToUpload == null) {
            return "Profile image must be uploaded.";
        }
        return  null;
    }

    @Override
    public void handleRegisterSuccess(User user, AuthToken token) {
        view.clearInfoMessage();
        view.clearErrorMessage();

        view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());
        view.navigateToUser(user);
    }

    @Override
    public void handleRegisterFailure(String message) {
        view.displayInfoMessage("Failed to register: " + message);
    }

    @Override
    public void handleRegisterThrewException(Exception ex) {
        view.displayInfoMessage("Failed to register because of exception: " + ex.getMessage());
    }


}
