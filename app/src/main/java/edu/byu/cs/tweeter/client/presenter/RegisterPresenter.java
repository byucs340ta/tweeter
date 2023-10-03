package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.services.RegisterService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter implements RegisterService.RegisterObserver {
    @Override
    public void registerSucceeded(User user, AuthToken authToken) {
        view.hideErrorMessage();
        view.hideInfoMessage();
        view.showInfoMessage("Hello, " + user.getName());
        view.openMainView(user);
    }

    @Override
    public void registerFailed(String message) {
        view.showErrorMessage(message);
    }

    public interface View {
        void showInfoMessage(String message);
        void hideInfoMessage();
        void showErrorMessage(String message);
        void hideErrorMessage();

        void openMainView(User user);
    }

    private final View view;

    public RegisterPresenter(View view) {
        this.view = view;
    }
    public void register(String firstName, String lastName, String alias, String password, Bitmap image) {
        if (!validateRegistration(firstName, lastName, alias, password, image)) {
            return;
        }
        view.hideErrorMessage();
        view.showInfoMessage("Registering...");

        // Convert image to byte array.
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        var registerService = new RegisterService();
        registerService.register(firstName, lastName, alias, password, imageBytesBase64, this);
    }


    private boolean validateRegistration(String firstName, String lastName, String alias, String password, Bitmap imageToUpload) {
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
}
