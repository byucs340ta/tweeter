package edu.byu.cs.tweeter.client.model.services.newservices;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.GetStatusesHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.handler.PostHandler;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.PostObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
    public void getFeed(User user, int pageSize, Status lastItem, PagedObserver<Status> observer) {
        GetFeedTask task = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(), user, pageSize, lastItem, new GetStatusesHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }

    public void getStory(User user, int pageSize, Status lastItem, PagedObserver<Status> observer) {
        GetStoryTask task = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(), user, pageSize, lastItem, new GetStatusesHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }

    public void postStatus(String post, PostObserver observer) {
        Status status = new Status(post, Cache.getInstance().getCurrUser(), System.currentTimeMillis(), parseURLs(post), parseMentions(post));
        PostStatusTask task = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(), status, new PostHandler(observer));
        BackgroundTaskUtils.runTask(task);
    }
    private List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    private int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }
}
