package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.AccountService;
import edu.byu.cs.tweeter.client.presenter.view.AuthView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends AuthPresenter {
    public RegisterPresenter(AuthView view) {
        super(view);
    }

    public void register(String firstName, String lastName, String alias, String password, String image) {
        String errorMessage = validateRegistration(firstName, lastName, alias, password, image);
        AuthView view = getView();
        if (errorMessage == null) {
            view.clearInfoMessage();
            view.clearErrorMessage();
            view.displayMessage("Registering...");
            accountService.register(firstName, lastName, alias, password, image, new RegisterObserver());
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
        String temp = validateLogin(alias, password);
        if (temp != null) {
            return temp;
        }
        if (imageToUpload == null) {
            return "Profile image must be uploaded.";
        }
        return  null;
    }

    public class RegisterObserver extends AuthObserver {
        @Override
        protected String getBaseMessage() {
            return "Failed to register";
        }
    }

}
