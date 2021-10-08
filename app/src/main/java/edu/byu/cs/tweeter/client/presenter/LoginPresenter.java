package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

// Class with all logic for the UI. This is our Presenter section baby.
// The idea is for any input user can do in the view, we need a corresponding method here in the presenter
public class LoginPresenter implements UserService.LoginObserver {

    public LoginPresenter(View view){
        this.view = view;
    }

    // Pointer to the main view
    private View view;

    // All the methods that the view needs to have so that the presenter can interact with it above.
    // This is the same way we're implementing the observer interface in Service. Names just differ. Interesting...
    public interface View {
        void navigateToUser(User user);

        void displayErrorMessage(String message);
        void clearErrorMessage();

        // this may look less ominous than error messages. So we made 2
        void displayInfoMessage(String message);
        void clearInfoMessage();
    }

    // The only method the VIEW can call on presenter
    public void login(String alias, String password) {

        view.clearErrorMessage();
        view.clearInfoMessage();

        String message = validateLogin(alias, password);
        if (message == null) {
            view.displayInfoMessage("Logging In...");
            new UserService().login(alias,password, this); // THIS IS AN OBSERVER WTF?!
        }
        else {
            view.displayErrorMessage("Login failed: " + message);
        }
    }

    private String validateLogin(String alias, String password) {
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }
        return null;
    }

    //****** Implementing the LoginObserver *******
    @Override
    public void loginSucceeded(AuthToken authToken, User user) {
        view.navigateToUser(user);
        view.clearErrorMessage();
        view.displayInfoMessage("Hello " + user.getName());
    }

    @Override
    public void loginFailed(String message) {
        view.displayErrorMessage("Login failed: " + message);
    }

    @Override
    public void loginThrewException(Exception ex) {
        view.displayErrorMessage("Login failed: " + ex.getMessage());
    }

}