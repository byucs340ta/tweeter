package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handler.AuthHandler;
import edu.byu.cs.tweeter.client.model.service.observer.AuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AccountService extends Service {
    public void login(String username, String password, AuthObserver observer) {
        LoginTask loginTask = new LoginTask(username, password, new AuthHandler(observer));
        executeTask(loginTask);
    }

    public void register(String firstName, String lastName, String username, String password, String image, AuthObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, username, password, image, new AuthHandler(observer));
        executeTask(registerTask);
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
