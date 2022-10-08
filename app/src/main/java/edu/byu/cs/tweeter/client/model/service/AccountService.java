package edu.byu.cs.tweeter.client.model.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AccountService {
    public interface LoginObserver {
        void handleLoginSuccess(User user, AuthToken authToken);
        void handleLoginFailure(String message);
        void handleLoginThrewException(Exception ex);
    }
    public void login(String username, String password, LoginObserver observer) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }
    private class LoginHandler extends Handler {
        private LoginObserver observer;
        public  LoginHandler(LoginObserver observer) {
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
                observer.handleLoginSuccess(loggedInUser, authToken);
            } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
                observer.handleLoginFailure(message);
            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
                observer.handleLoginThrewException(ex);
            }
        }
    }

    public interface RegisterObserver {
        void handleRegisterSuccess(User user, AuthToken token);
        void handleRegisterFailure(String message);
        void handleRegisterThrewException(Exception ex);
    }
    public void register(String firstName, String lastName, String username, String password, String image, RegisterObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, username, password, image, new RegisterHandler(observer));
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

                observer.handleRegisterSuccess(registeredUser, authToken);
            } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
                observer.handleRegisterFailure(message);
            } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
                observer.handleRegisterThrewException(ex);
            }
        }
    }

    public interface LogoutObserver {
        void handleLogoutSuccess();
        void handleLogoutFailure(String message);
        void handleLogoutThrewException(Exception ex);
    }
    public void logout(AuthToken token, LogoutObserver observer) {
        LogoutTask logoutTask = new LogoutTask(token, new LogoutHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }
    private class LogoutHandler extends Handler {
        LogoutObserver observer;
        public LogoutHandler(LogoutObserver observer) {
            this.observer = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
            if (success) {
                observer.handleLogoutSuccess();
            } else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);
                observer.handleLogoutFailure(message);
                } else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);
                observer.handleLogoutThrewException(ex);
            }
        }
    }
}
