package edu.byu.cs.tweeter.client.model.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

//    public interface GetUserObserver {
//        void handleSuccess();
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }
//
//    public void getUser(String userAlias, GetUserObserver getUserObserver) {
//        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
//                userAlias, new GetUserHandler(getUserObserver));
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getUserTask);
//    }
//
//    /**
//     * Message handler (i.e., observer) for GetUserTask.
//     */
//    private class GetUserHandler extends Handler {
//
//        private GetUserObserver observer;
//
//        public GetUserHandler(GetUserObserver observer) {
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
//            if (success) {
//                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
//
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
//                startActivity(intent);
//            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
//                observer.handleFailure(message);
//                Toast.makeText(getContext(), "Failed to get user's profile: " + message, Toast.LENGTH_LONG).show();
//            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
//                observer.handleException(ex);
//                Toast.makeText(getContext(), "Failed to get user's profile because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}
