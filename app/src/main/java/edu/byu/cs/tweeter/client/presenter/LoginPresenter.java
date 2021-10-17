package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.SignInObserver;
import edu.byu.cs.tweeter.client.presenter.presenter.views.NavigateToUserView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

// Class with all logic for the UI. This is our Presenter section baby.
// The idea is for any input user can do in the view, we need a corresponding method here in the presenter
public class LoginPresenter implements SignInObserver {

    public LoginPresenter(NavigateToUserView view){
        this.view = view;
    }

    // Pointer to the main view
    private NavigateToUserView view;

    // All the methods that the view needs to have so that the presenter can interact with it above.
    // This is the same way we're implementing the observer interface in Service. Names just differ. Interesting...
//    public interface View {
//        void navigateToUser(User user);
//
//        void displayErrorMessage(String message);
//        void clearErrorMessage();
//
//        // this may look less ominous than error messages. So we made 2
//        void displayInfoMessage(String message);
//        void clearInfoMessage();
//    }

    //************************** Implementing the LoginObserver ****************************//
    /////////////////////////////////////////////////////////////////////////////////////////

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

    @Override
    public void SignInSucceeded(AuthToken authToken, User user) {
        view.navigateToUser(user);
        view.clearErrorMessage();
        view.displayInfoMessage("Hello " + user.getName());
    }

    @Override
    public void serviceFailure(String message) {
        view.displayErrorMessage(message);
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

}
