package edu.byu.cs.tweeter.client.presenter;

import java.util.Base64;

import edu.byu.cs.tweeter.client.model.services.newservices.UserService;

public class RegisterPresenter extends AuthenticatePresenter {
    public RegisterPresenter(View view) {
        super(view);
        this.view = view;
    }
    public void register(String firstName, String lastName, String alias, String password, byte[] imageBytes) {
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);
        if (!validateRegistration(firstName, lastName, alias, password, imageBytesBase64)) {
            return;
        }
        view.hideErrorMessage();
        view.showInfoMessage("Registering...");

        var registerService = new UserService();
        registerService.register(firstName, lastName, alias, password, imageBytesBase64, this);
    }


    private boolean validateRegistration(String firstName, String lastName, String alias, String password, String imageToUpload) {
        view.hideErrorMessage();
        if (firstName.length() == 0) {
            view.showErrorMessage("First Name cannot be empty.");
            return false;
        }
        if (lastName.length() == 0) {
            view.showErrorMessage("Last Name cannot be empty.");
            return false;
        }
        if (alias.length() == 0) {
            view.showErrorMessage("Alias cannot be empty.");
            return false;
        }
        if (alias.charAt(0) != '@') {
            view.showErrorMessage("Alias must begin with @.");
            return false;
        }
        if (alias.length() < 2) {
            view.showErrorMessage("Alias must contain 1 or more characters after the @.");
            return false;
        }
        if (password.length() == 0) {
            view.showErrorMessage("Password cannot be empty.");
            return false;
        }

        if (imageToUpload == null) {
            view.showErrorMessage("Profile image must be uploaded.");
            return false;
        }
        return true;
    }

    @Override
    public void handleFailure(String message) {
        view.showErrorMessage("Failed to register:" + message);
    }

    @Override
    public void handleException(Exception exception) {
        view.showErrorMessage("Failed to register because of exception: " + exception.getMessage());
    }
}
