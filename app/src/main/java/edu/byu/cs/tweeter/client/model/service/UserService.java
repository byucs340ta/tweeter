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
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    //todo: Add logout
    // todo: Add register

    //*********************************** Login **************************************

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

    //*********************************** GetUser **************************************

    /**
     * Interface observer which is used to send back information when "getUser()" is called from
     * above.
     */
    public interface GetUserObserver {
        void getUserSucceeded(User user);
        void getUserFailed(String message);
        void getUserThrewException(Exception ex);
    }

    /**
     * Gets a user if permissions are valid
     * @param authToken Current user needs to be authorized to access this information
     * @param alias How we know which user the current action wants to get
     */
    public void getUser(AuthToken authToken, String alias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(authToken, alias, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     * Recieves message from the task that has data saying how the operation went: success/fail
     * On success it contains a user. This is like the output of the background task. A map.
     * Most of what this does is call observers above.
     */
    private class GetUserHandler extends Handler {

        private GetUserObserver observer;

        //
        public GetUserHandler(GetUserObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.getUserSucceeded(user);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                observer.getUserFailed(message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                observer.getUserThrewException(ex);
            }
        }
    }

}
