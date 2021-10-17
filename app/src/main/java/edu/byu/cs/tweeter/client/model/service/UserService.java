package edu.byu.cs.tweeter.client.model.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handler.GetUserTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.view.login.RegisterFragment;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    //*********************************** Register **************************************
    //todo: M2B -- Eliminate duplicate code by adding classes and using more inheritance

    /**
     * Observer will be instantiated in the RegisterPresenter where logic for how to handle
     * these results is written
     */
    public interface RegisterObserver {
        void registerSucceeded(AuthToken authToken, User user);
        void registerFailed(String message);
        void registerThrewException(Exception ex);
    }

    /**
     * Sends the requests for information using a handler
     */
    public void register(String firstName, String lastName, String alias, String password,
                         String imageBytesBase64, RegisterObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new RegisterHandler(observer));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }

    private class RegisterHandler extends Handler {
        private RegisterObserver observer;

        public RegisterHandler(RegisterObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
            if (success) {
                User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);
                Cache.getInstance().setCurrUser(registeredUser);
                Cache.getInstance().setCurrUserAuthToken(authToken);
                observer.registerSucceeded(authToken, registeredUser);
            } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
                observer.registerFailed(message);
            } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
                observer.registerThrewException(ex);
            }
        }
    }

    //*********************************** Login **************************************
    //todo: M2B -- Eliminate duplicate code by adding classes and using more inheritance

    /**
     * The Login function is void, but sets an asynchronous task at work, so we need some way to
     * get information when the task is done. IE, observer interface! In the presenter we will
     * implement this interface so that at it's correct layer we can define correct behavior ,
     * and will pass in the next level implementation of it to be used in this Login function.
     */
    public interface LoginObserver {
        void loginSucceeded(AuthToken authToken, User user);
        void loginFailed(String message);
        void loginThrewException(Exception ex);
    }

    /**
     * Presenter one layer up calls this function. It passes in an observer (interfaced on this layer
     * but defined in the presenter) so that we can interact with it, allowing indirect communication
     * with above layer.
     * @param alias
     * @param password
     * @param observer
     */
    public void login(String alias, String password, LoginObserver observer) {
        // Run a LoginTask to login the user
        LoginTask loginTask = new LoginTask(alias, password,
                new LoginHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }


    /**
     * Message handler (i.e., observer) for LoginTask
     * Login handler is going to start our login task! But we need a way to communicate back to
     * the presenter when it's finished. Hence, we take an observer interface in that they define!
     */
    private class LoginHandler extends Handler {

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
            } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
                observer.loginFailed(message); // propagate up!
            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
                observer.loginThrewException(ex); // propagate up!
            }
        }
    }



    //*********************************** GetUser **************************************//
    /////////////////////////////////////////////////////////////////////////////////////

    public void getUser(AuthToken authToken, String alias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(new GetUserTaskHandler(observer), authToken, alias);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }



    //*********************************** Logout **************************************//
    ////////////////////////////////////////////////////////////////////////////////////

    public void logout(AuthToken authToken, LogoutObserver observer) {
        // Run a LoginTask to login the user
        LogoutTask logoutTask = new LogoutTask(new LogoutHandler(observer), authToken);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }

}
