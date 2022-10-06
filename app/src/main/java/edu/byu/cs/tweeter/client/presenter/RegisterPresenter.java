package edu.byu.cs.tweeter.client.presenter;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class RegisterPresenter extends AuthenticatePresenter {

    public RegisterPresenter(AuthenticateView view) {
        super(view, "register");
    }

    public void register(String firstName, String lastName, String alias,
                         String password, BitmapDrawable imageToUpload) {

        view.AuthenticatedToast(true);
        userService.registerUser(firstName, lastName, alias, password, imageToUpload, new GetAuthenticateObserver());
    }

    public void validateRegistration(String firstName, String lastName, String alias,
                                     String password, Drawable imageToUpload) {

        validateCredentials(alias, password);

        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (imageToUpload == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }
}