package presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;


public class MainPresenterTestDemo {
    private MainPresenter mainPresenterFullMock;

    @Before
    public void setup() {
        mainPresenterFullMock = Mockito.mock(MainPresenter.class);
    }

    @Test
    public void testLogin_logoutSucceeds() {

        // Setup the test case
        Answer<Void> logoutSucceededAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                LogoutObserver observer = mainPresenterFullMock;
                observer.LogoutSucceeded(); // Pretend it succeeded. We never called the observer.
                return null;
            }
        };

        Mockito.doAnswer(logoutSucceededAnswer).when(mainPresenterFullMock).logout();

        // Run the test case
        mainPresenterFullMock.logout();

        // Interview
        Mockito.verify(mainPresenterFullMock).LogoutSucceeded();
    }

    @Test
    public void testLogin_logoutFails() {

        // Setup the test case
        Answer<Void> logoutFailedAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                LogoutObserver observer = mainPresenterFullMock;
                observer.serviceFailure("Login Failed"); // Pretend it succeeded. We never called the observer.
                return null;
            }
        };

        Mockito.doAnswer(logoutFailedAnswer).when(mainPresenterFullMock).logout();

        // Run the test case
        mainPresenterFullMock.logout();

        // Interview
        Mockito.verify(mainPresenterFullMock).serviceFailure("Login Failed");
    }


}
