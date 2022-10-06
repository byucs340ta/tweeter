package edu.byu.cs.tweeter.client.presenter;

public class LoginPresenter extends AuthenticatePresenter {

    public LoginPresenter(AuthenticateView view) {
        super(view, "login");
    }

    public void login(String alias, String password) {
        view.AuthenticatedToast(true);
        userService.loginUser(alias, password, new GetAuthenticateObserver());
    }
}