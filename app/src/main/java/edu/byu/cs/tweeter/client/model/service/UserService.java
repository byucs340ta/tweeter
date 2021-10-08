package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    // The Login function is void, but sets an asynchronous task at work, so we need some way to
    // get information when the task is done. IE, observer interface! In the presenter we will
    // implement this interface so that at it's correct layer we can define correct behavior ,
    // and will pass in the next level implementation of it to be used in this Login function.
    // todo: I would rename this LoginObserverInterface
    public interface LoginObserver {
        void loginSucceeded(AuthToken authToken, User user);
        void loginFailed(String message);
        void loginThrewException(Exception ex);
    }

    // Presenter will be calling this in the layer up!
    public void login(String alias, String password, LoginObserver observer) {
        // Run a LoginTask to login the user
        LoginTask loginTask = new LoginTask(alias, password,
                new LoginHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends Handler {
        // Login handler is going to start our login task! But we need a way to communicate back to
        // the presenter when it's finished. Hence, we take an observer interface in that they define!
        private LoginObserver observer;

        public LoginHandler(LoginObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
            if (success) {
                User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

                // Cache user session information
                Cache.getInstance().setCurrUser(loggedInUser);
                Cache.getInstance().setCurrUserAuthToken(authToken);

                observer.loginSucceeded(authToken, loggedInUser); // propagate up!
//
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                intent.putExtra(MainActivity.CURRENT_USER_KEY, loggedInUser);
//
//                loginInToast.cancel();
//
//                Toast.makeText(getContext(), "Hello " + Cache.getInstance().getCurrUser().getName(), Toast.LENGTH_LONG).show();
//                startActivity(intent);
            } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
                observer.loginFailed(message); // propagate up!
//                Toast.makeText(getContext(), "Failed to login: " + message, Toast.LENGTH_LONG).show();
            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
                observer.loginThrewException(ex); // propagate up!
//                Toast.makeText(getContext(), "Failed to login because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
