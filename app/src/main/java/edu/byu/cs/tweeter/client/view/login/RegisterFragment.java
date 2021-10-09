package edu.byu.cs.tweeter.client.view.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the register screen.
 */
public class RegisterFragment extends Fragment implements RegisterPresenter.View {
    private static final String LOG_TAG = "RegisterFragment";
    private static final int RESULT_IMAGE = 10;

    private EditText firstName;
    private EditText lastName;
    private EditText alias;
    private EditText password;
    private Button imageUploaderButton;
    private Button registerButton;
    private ImageView imageToUpload;
    private TextView errorView;
    private Toast registeringToast;

    private RegisterPresenter presenter = new RegisterPresenter(this);

    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @return the fragment.
     */
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        firstName = view.findViewById(R.id.registerFirstName);
        lastName = view.findViewById(R.id.registerLastName);
        alias = view.findViewById(R.id.registerUsername);
        password = view.findViewById(R.id.registerPassword);
        imageUploaderButton = view.findViewById(R.id.imageButton);
        imageToUpload = view.findViewById(R.id.registerImage);
        registerButton = view.findViewById(R.id.registerButton);
        errorView = view.findViewById(R.id.registerError);

        imageUploaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, RESULT_IMAGE);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Register and move to MainActivity.
                    presenter.register(firstName.getText().toString(),
                            lastName.getText().toString(), alias.getText().toString(),
                            password.getText().toString(), imageToUpload.getDrawable());
            }
        });

        return view;
    }

    // Get image if uploaded from gallery.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
            imageUploaderButton.setText(R.string.afterUploadPicture);
        }
    }

    @Override
    public void navigateToUser(User registeredUser) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, registeredUser);
        startActivity(intent);
    }

    @Override
    public void displayErrorMessage(String message) {
        errorView.setText(message);
    }

    @Override
    public void clearErrorMessage() {
        errorView.setText("");
    }

    @Override
    public void displayInfoMessage(String message) {
        clearInfoMessage();
        registeringToast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        registeringToast.show();
    }

    @Override
    public void clearInfoMessage() {
        if (registeringToast != null) {
            registeringToast.cancel();
            registeringToast = null;
        }
    }

    // RegisterHandler

//    private class RegisterHandler extends Handler {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
//            if (success) {
//                User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
//                AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);
//
//                Intent intent = new Intent(getContext(), MainActivity.class);
//
//                Cache.getInstance().setCurrUser(registeredUser);
//                Cache.getInstance().setCurrUserAuthToken(authToken);
//
//                intent.putExtra(MainActivity.CURRENT_USER_KEY, registeredUser);
//
//                registeringToast.cancel();
//
//                Toast.makeText(getContext(), "Hello " + Cache.getInstance().getCurrUser().getName(), Toast.LENGTH_LONG).show();
//                try {
//                    startActivity(intent);
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
//                Toast.makeText(getContext(), "Failed to register: " + message, Toast.LENGTH_LONG).show();
//            } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
//                Toast.makeText(getContext(), "Failed to register because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }

}
