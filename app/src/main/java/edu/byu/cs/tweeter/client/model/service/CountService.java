package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.handler.CountTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class CountService extends BaseService{

public void countFollowersAndFollowing(AuthToken authToken, User targetUser, CountObserver observer) {
    GetCountTask countTask = new GetCountTask(new CountTaskHandler(observer),
            authToken, targetUser);
    super.executeService(countTask);
    }

}
