package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter implements UserService.RegisterObserver {

    public RegisterPresenter(View view) { this.view = view; }

    private View view;

    public interface View {
        void navigateToUser(User user);

        void displayErrorMessage(String message);
        void clearErrorMessage();

        // this may look less ominous than error messages. So we made 2
        void displayInfoMessage(String message);
        void clearInfoMessage();
    }

    // The only method the VIEW can call on presenter
    public void register(String firstName, String lastName, String alias, String password,
                         Drawable profileDrawableImage) {

        view.clearErrorMessage();
        view.clearInfoMessage();

        String message = validateRegistration(firstName, lastName, alias,
                password, profileDrawableImage);
        if (message == null) {

            // Convert image to byte array.
            Bitmap image = ((BitmapDrawable) profileDrawableImage).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();
            String imageBytesBase64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

            view.displayInfoMessage("Registering...");
            new UserService().register(firstName, lastName, alias, password,
                    imageBytesBase64,this);
        }
        else {
            view.displayErrorMessage("Register failed: " + message);
        }
    }

    public String validateRegistration(String firstName, String lastName, String alias,
                                     String password, Drawable profileDrawableImage) {
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
        if (profileDrawableImage == null) {
            return "Profile image must be uploaded.";
        }
        return null;
    }

    //************************ Implement Register Observer ***********************


    @Override
    public void registerSucceeded(AuthToken authToken, User user) {
        view.navigateToUser(user);
        view.clearErrorMessage();
        view.displayInfoMessage("Hello " + user.getName());
    }

    @Override
    public void registerFailed(String message) {
        view.displayErrorMessage("Failed to register: " + message);
    }

    @Override
    public void registerThrewException(Exception ex) {
        view.displayErrorMessage("Failed to register: " + ex.getMessage());
    }
}
