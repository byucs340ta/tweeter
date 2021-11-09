package presenter;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//
//import edu.byu.cs.tweeter.client.model.service.PostService;
//import edu.byu.cs.tweeter.client.presenter.MainPresenter;
//import edu.byu.cs.tweeter.model.domain.AuthToken;
//import edu.byu.cs.tweeter.model.domain.User;
//
public class PostStatusTest {
//    private MainPresenter.View mockMainView;
//    private MainPresenter spyMainPresenter;
//    private PostService mockPostService;
//
//    @Before
//    public void setup() {
//        mockMainView = Mockito.mock(MainPresenter.View.class);
//        mockPostService = Mockito.mock(PostService.class);
//
//        spyMainPresenter = Mockito.spy(new MainPresenter(mockMainView, new AuthToken(), new User()));
//
//        Mockito.doReturn(mockPostService).when(spyMainPresenter).postStatus(Mockito.any(), Mockito.any(),
//                Mockito.any(), Mockito.any(), Mockito.any()); // todo: is this ok...?
//    }
//
//    @Test
//    public void testPostStatus_postSucceed() {
//        Answer<Void> postSucceededAnswer = new Answer<Void>() {
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable {
//                spyMainPresenter.PostSucceeded();
//                return null;
//            }
//        };
//
//        Mockito.doAnswer(postSucceededAnswer).when(spyMainPresenter).postStatus(Mockito.any(),
//                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
//
//        spyMainPresenter.postStatus(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
//                Mockito.any());
//
//        Mockito.verify(spyMainPresenter).PostSucceeded();
//    }
//
//
//
////    @Test
////    public void testPostStatus_postFailure() {
////        Answer<Void> postFailedAnswer = new Answer<Void>() {
////            @Override
////            public Void answer(InvocationOnMock invocation) throws Throwable {
////                mockMainPresenter.serviceFailure("Failed to post status: <ERROR MESSAGE>");
////                return null;
////            }
////        };
////
////        Mockito.doAnswer(postFailedAnswer).when(mockMainPresenter).postStatus(Mockito.any(),
////                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
////
////        mockMainPresenter.postStatus(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
////                Mockito.any());
////
////        Mockito.verify(mockMainPresenter).serviceFailure("Failed to post status: <ERROR MESSAGE>");
////    }
////
////
////
////    @Test
////    public void testPostStatus_postException() {
////        Answer<Void> postExceptionAnswer = new Answer<Void>() {
////            @Override
////            public Void answer(InvocationOnMock invocation) throws Throwable {
////                mockMainPresenter
////                        .serviceException("Failed to post status because of exception: <EXCEPTION MESSAGE>");
////                return null;
////            }
////        };
////
////        Mockito.doAnswer(postExceptionAnswer).when(mockMainPresenter).postStatus(Mockito.any(),
////                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
////
////        mockMainPresenter.postStatus(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
////                Mockito.any());
////
////        Mockito.verify(mockMainPresenter)
////                .serviceException("Failed to post status because of exception: <EXCEPTION MESSAGE>");
////    }
//
}
