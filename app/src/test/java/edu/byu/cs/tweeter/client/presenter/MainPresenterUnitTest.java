package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.PostService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PostObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainPresenterUnitTest {

    private MainPresenter.View mockView;
    private UserService mockUserService;
    private PostService mockPostService;
    private Cache mockCache;

    private MainPresenter mainPresenterSpy;


    @BeforeEach
    public void setup() {
        // Create mock dependencies
        mockView = Mockito.mock(MainPresenter.View.class);
        mockUserService = Mockito.mock(UserService.class);
        mockCache = Mockito.mock(Cache.class); // To mock, constructor must be public. The problem with statics is they aren't unit testable.
        Cache.setInstance(mockCache);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView, new AuthToken(), new User()));
        Mockito.doReturn(mockUserService).when(mainPresenterSpy).getUserService(); // We use spy to take over methods like this.

        mockPostService = Mockito.mock(PostService.class);
        Mockito.doReturn(mockPostService).when(mainPresenterSpy).getPostService();
    }



    @Test
    public void testLogout_logoutSucceeds() {
        Answer<Void> successAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                LogoutObserver observer = invocation.getArgumentAt(1, LogoutObserver.class);
                observer.LogoutSucceeded();
                return null;
            }
        };

        // Rewrite logout()
        Mockito.doAnswer(successAnswer).when(mockUserService).logout(Mockito.any(), Mockito.any());

        // Test...
        mainPresenterSpy.logout();

        // Report
        Mockito.verify(mockView).displayInfoMessage("Logging Out...");

        Mockito.verify(mockCache).clearCache();
        Mockito.verify(mockView).clearInfoMessage();
        Mockito.verify(mockView).logout();
    }



    @Test
    public void testLogout_logoutFails() {
        Answer<Void> failureAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                LogoutObserver observer = invocation.getArgumentAt(1, LogoutObserver.class);
                observer.serviceFailure("Failed");
                return null;
            }
        };

        // Rewrite logout()
        Mockito.doAnswer(failureAnswer).when(mockUserService).logout(Mockito.any(), Mockito.any());

        // Test...
        mainPresenterSpy.logout();

        // Report
        Mockito.verify(mockView).displayInfoMessage("Logging Out...");
        Mockito.verify(mockView).displayErrorMessage("Service failed: Failed");
        Mockito.verify(mockCache, Mockito.times(0)).clearCache();
        Mockito.verify(mockView, Mockito.times(0)).logout();
    }



    @Test
    public void testLogout_logoutException() {
        Answer<Void> exceptionAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                LogoutObserver observer = invocation.getArgumentAt(1, LogoutObserver.class);
                observer.serviceException("Exception");
                return null;
            }
        };

        // Rewrite logout()
        Mockito.doAnswer(exceptionAnswer).when(mockUserService).logout(Mockito.any(), Mockito.any());

        // Test...
        mainPresenterSpy.logout();

        // Report
        Mockito.verify(mockView).displayInfoMessage("Logging Out...");
        Mockito.verify(mockView).displayErrorMessage("Service failed because of exception: Exception");
        Mockito.verify(mockCache, Mockito.times(0)).clearCache();
        Mockito.verify(mockView, Mockito.times(0)).logout();
    }



    @Test
    public void testPostStatus_postSucceeds() {
        Answer<Void> successAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                PostObserver observer = invocation.getArgumentAt(1, PostObserver.class);
                observer.PostSucceeded();
                return null;
            }
        };

        // Rewrite run() for post
        Mockito.doAnswer(successAnswer).when(mockPostService).run(Mockito.any(), Mockito.any());

        // Test...
        mainPresenterSpy.postStatus(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        // Report
        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).displayInfoMessage("Successfully Posted!");
    }

    @Test
    public void testPostStatus_postFails() {
        Answer<Void> failsAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                PostObserver observer = invocation.getArgumentAt(1, PostObserver.class);
                observer.serviceFailure("<ERROR MESSAGE>");
                return null;
            }
        };

        // Rewrite run() for post
        Mockito.doAnswer(failsAnswer).when(mockPostService).run(Mockito.any(), Mockito.any());

        // Test...
        mainPresenterSpy.postStatus(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        // Report
//        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).displayErrorMessage("Service failed: <ERROR MESSAGE>");
    }

    @Test
    public void testPostStatus_postException() {
        Answer<Void> exceptionAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                PostObserver observer = invocation.getArgumentAt(1, PostObserver.class);
                observer.serviceException("<EXCEPTION MESSAGE>");
                return null;
            }
        };

        // Rewrite run() for post
        Mockito.doAnswer(exceptionAnswer).when(mockPostService).run(Mockito.any(), Mockito.any());

        // Test...
        mainPresenterSpy.postStatus(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        // Report
//        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).displayErrorMessage("Service failed because of exception: <EXCEPTION MESSAGE>");
    }

    // Todo: verify somehow?
//    @Test
//    public void verify_ifCorrect() {
//        String mockStatus = "Mock Status";
//        User mockUser = Mockito.mock(User.class);
//        String mockDate = "dd-mm-yy";
//
//        List<String> mockURLs = new ArrayList<String>();
//        mockURLs.add("1");
//        mockURLs.add("2");
//        mockURLs.add("3");
//
//        List<String> mockMentions = new ArrayList<String>();
//        mockMentions.add("@1");
//        mockMentions.add("@2");
//        mockMentions.add("@3");
//
////        PostObserver mockPostObserver = Mockito.mock(PostObserver.class);
//
//        mainPresenterSpy.postStatus(mockStatus, mockUser, mockDate, mockURLs, mockMentions);
//
//        Mockito.doAnswer(invocation -> {
//            PostObserver observer = (PostObserver) invocation.getArguments()[1];
//            assertEquals(invocation.getArguments()[0], mockStatus);
//            assertEquals(invocation.getArguments()[1], mockPostObserver);
//            observer.PostSucceeded();
//            return true;
//        }).when(mockPostService).run(mockStatus, mockPostObserver);
//    }

}
