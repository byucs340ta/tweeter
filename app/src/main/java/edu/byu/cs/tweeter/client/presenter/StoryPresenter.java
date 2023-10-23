package edu.byu.cs.tweeter.client.presenter;


import edu.byu.cs.tweeter.client.model.services.newservices.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {
    @Override
    protected void getItems() {
        StatusService service = new StatusService();
        this.errorMessage = "story";
        service.getStory(user, PAGE_SIZE, lastItem, this);
    }

    public interface StoryView extends PagedView<Status> {}

    public StoryPresenter(StoryView view, User user) {
        super(view, user);
    }
}
