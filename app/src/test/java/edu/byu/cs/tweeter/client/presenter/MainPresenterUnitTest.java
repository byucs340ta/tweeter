package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class MainPresenterUnitTest {

    private MainPresenter.MainView mockView;
    private Cache mockCache;
    private UserService mockUserService;

    private MainPresenter mainPresenterSpy;

    @BeforeEach
    public void setup() {

        // create mocks
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockCache = Mockito.mock(Cache.class);
        mockUserService = Mockito.mock(UserService.class);

        Cache.setInstance(mockCache);
        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));

        // This says to return the user service when the mainPresenterSpy has its getUserService method called
//        Mockito.doReturn(mockUserService).when(mainPresenterSpy).getUserService();
        //Type checking form of the same thing
        Mockito.when(mainPresenterSpy.getUserService()).thenReturn(mockUserService);
    }

    @Test
    public void testLogout_logoutSuccessful() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.LogoutObserver observer = invocation.getArgument(1, MainPresenter.LogoutObserver.class);
                observer.handleSuccess();
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockUserService).logoutUser(Mockito.any(), Mockito.any());
        mainPresenterSpy.logoutUser();

        //Mockito.verify(mockView).displayMessage("Logging Out...");
        Mockito.verify(mockView).postLogoutUser();
        Mockito.verify(mockCache).clearCache();

        //Mockito.verify(mockView).clearInfoMessage();
        Mockito.verify(mockView).postLogoutUser();
    }

    @Test
    public void testLogout_logoutFailed() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.LogoutObserver observer = invocation.getArgument(1, MainPresenter.LogoutObserver.class);
                observer.displayErrorMessage("something bad happened");
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockUserService).logoutUser(Mockito.any(), Mockito.any());
        mainPresenterSpy.logoutUser();

        Mockito.verify(mockCache, Mockito.times(0)).clearCache();

        Mockito.verify(mockView).displayMessage("Failed to logout: something bad happened");
    }

    @Test
    public void testLogout_logoutFailedWithException() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.LogoutObserver observer = invocation.getArgument(1, MainPresenter.LogoutObserver.class);
                observer.displayException(new Exception("My Exception"));
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockUserService).logoutUser(Mockito.any(), Mockito.any());
        mainPresenterSpy.logoutUser();

        Mockito.verify(mockCache, Mockito.times(0)).clearCache();

        //Mockito.verify(mockView).clearInfoMessage();
        Mockito.verify(mockView).displayMessage("Failed to logout because of exception: My Exception");
    }
}
