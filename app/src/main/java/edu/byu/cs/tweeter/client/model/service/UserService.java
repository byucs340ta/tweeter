package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {
    public interface GetUserObserver {
        void handleGetUserSuccess(User user);
        void handleGetUserFailed(String message);
        void handleGetUserThrewException(Exception e);
    }
    public void getUser(AuthToken token, String alias, GetUserObserver observer){
        GetUserTask getUserTask = new GetUserTask(token, alias, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }
    private class GetUserHandler extends Handler{
        private GetUserObserver observer;

        public GetUserHandler(GetUserObserver observer){
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg){
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.handleGetUserSuccess(user);
            }
            else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)){
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                observer.handleGetUserFailed(message);
            }
            else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)){
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                observer.handleGetUserThrewException(ex);
            }
        }
    }
}
