package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.byu.cs.tweeter.client.model.services.StatusService;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.utils.StatusUtil;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

// When a user clicks the "POST STATUS" button, the View should call a "post status" operation on a Presenter that posts the status to the server (although there isn't actually a server yet). Next, the Presenter's "post status" operation should:

// 1. Instruct the View to display a "Posting Status..." message.
// 2. Create a Status object and call a Service to send it to the server.
// 3. Instruct the View to display one of the following messages telling the user the outcome of the post operation:
//     I. "Successfully Posted!"
//     II. "Failed to post status: <ERROR MESSAGE>"
//     III. "Failed to post status because of exception: <EXCEPTION MESSAGE>"

// Using JUnit and Mockito, write automated UNIT tests to verify the following:

// 1. The Presenter's "post status" operation works correctly in all three of the outcomes listed above
// (succeeded, failed, failed due to exception).
// 2. All parameters passed by the Presenter to the Service's "post status" operation are correct (Right type and right value).

// This will require you to create mocks/spies for the View, Presenter, and Service.

public class PostStatusTest {
    @Mock
    private MainPresenter.View view;

    private MainPresenter presenter;

    @Mock
    private StatusService statusService;

//    @Mock
//    private PostStatusTask task;

//    @Mock
//    private PostObserver postObserver;
    private String post = "test";
    private User user = null;
    private long time = 100;
    private Status status;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        presenter = Mockito.spy(new MainPresenter(view, null));
        Mockito.when(presenter.createStatusService()).thenReturn(statusService);
        status = new Status(post, user, time, StatusUtil.parseURLs(post), StatusUtil.parseMentions(post));
    }

    @Test
    public void postStatusSuccess() {
        presenter.postStatus(post, user, time);
        Mockito.verify(view).showInfoMessage("Posting Status...");
        Mockito.verify(statusService).postStatus(status, presenter);
        presenter.postSucceeded();
        Mockito.verify(view).showInfoMessage("Successfully Posted!");
    }

    @Test
    public void postStatusHandleFailure() {
        presenter.postStatus(post, user, time);
        Mockito.verify(view).showInfoMessage("Posting Status...");
        Mockito.verify(statusService).postStatus(status, presenter);
        presenter.handleFailure("<ERROR MESSAGE>");
        Mockito.verify(view).showErrorMessage("Failed to post status: <ERROR MESSAGE>");
    }

    @Test
    public void postStatusHandleException() {
        presenter.postStatus(post, user, time);
        Mockito.verify(view).showInfoMessage("Posting Status...");
        Mockito.verify(statusService).postStatus(status, presenter);
        presenter.handleException(new Exception("<EXCEPTION MESSAGE>"));
        Mockito.verify(view).showErrorMessage("Failed to post status because of exception: <EXCEPTION MESSAGE>");
    }

}
