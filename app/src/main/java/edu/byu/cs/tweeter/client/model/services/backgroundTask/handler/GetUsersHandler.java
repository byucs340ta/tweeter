package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUsersHandler extends PagedHandler<User> {
    public GetUsersHandler(PagedObserver<User> observer) {
        super(observer);
    }
}
