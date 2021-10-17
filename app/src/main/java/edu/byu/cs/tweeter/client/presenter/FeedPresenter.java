package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.presenter.presenter.views.WebsiteView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> implements PagedObserver<Status>, GetUserObserver {
    private static final String LOG_TAG = "FeedPresenter";

    WebsiteView view;

    public FeedPresenter(WebsiteView view, AuthToken authToken, User targetUser) {
        super(view, authToken, targetUser);
        this.view = view;
    }

    // This runs get service when it comes up in page.
    @Override
    protected void runGetService() {
        new FeedService().getFeed(authToken, targetUser, PAGE_SIZE, lastItem, this);
    }

    public void evaluateClickedItem(String clickable) {
        if (clickable.contains("http")) {
            view.navigateToWebsite(clickable);
        } else {
            goToUser(clickable);
        }
    }

}
