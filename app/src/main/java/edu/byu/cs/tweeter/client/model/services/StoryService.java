package edu.byu.cs.tweeter.client.model.services;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryService {
    public interface GetStoryObserver {
        void getStorySucceeded(List<Status> statuses, boolean hasMorePages, Status lastStatus);

        void getStoryFailed(String message);
    }

    public void getStory(AuthToken authToken, User user, int pageSize, Status lastStatus, GetStoryObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(authToken,
                user, pageSize, lastStatus, new GetStoryHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }

    private static class GetStoryHandler extends Handler {
        private final GetStoryObserver observer;

        public GetStoryHandler(GetStoryObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
//            isLoading = false;
//            removeLoadingFooter();

            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);

                Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;

//                storyRecyclerViewAdapter.addItems(statuses);
                observer.getStorySucceeded(statuses, hasMorePages, lastStatus);
            } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
                observer.getStoryFailed("Failed to get story: " + message);
            } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
                observer.getStoryFailed("Failed to get story because of exception: " + ex.getMessage());
            }
        }
    }
}
