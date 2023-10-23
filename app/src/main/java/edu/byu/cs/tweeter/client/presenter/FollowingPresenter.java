package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.services.newservices.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {
    @Override
    protected void getItems() {
        FollowService service = new FollowService();
        this.errorMessage = "following";
        service.getFollowing(user, PAGE_SIZE, lastItem, this);
    }

    public interface FollowingView extends PagedView<User> {}

    public FollowingPresenter(FollowingView view, User user) {
        super(view, user);
    }
}

