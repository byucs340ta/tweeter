package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.StatusListRequest;
import edu.byu.cs.tweeter.model.net.response.FollowListResponse;
import edu.byu.cs.tweeter.model.net.response.StatusListResponse;
import edu.byu.cs.tweeter.server.dao.*;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class UserDAOTest {
    UserDAO dao = new UserDAO();
    AuthTokenDAO authTokenDAO = new AuthTokenDAO();

    @Test
    public void testCreateUser() {
//        dao.createUser(new User("First", "Last", "fake_url"), "fake_password");
//        AuthToken testToken = new AuthToken("this is a test");
//        testToken.setAlias("fake alias");
//        authTokenDAO.createAuthToken(testToken);
//        AuthToken dbToken = authTokenDAO.getAuthToken(testToken.getToken());
//        System.out.println(dbToken.getDatetime());
//        authTokenDAO.updateAuthToken(testToken.getToken());
//        dbToken = authTokenDAO.getAuthToken(testToken.getToken());
//        System.out.println(dbToken.getDatetime());

        DAOFactory daoFactory = new DAOFactory();
//        StoryDAO storyDAO = (StoryDAO) daoFactory.create("StoryDAO");
//        DateFormat dateFormat = DateFormat.getDateTimeInstance();
//
//        String datetime = dateFormat.format(new Date().getTime());
//        User testUser = new User("First", "Last", "fake_url");
//        ArrayList<String> testList = new ArrayList<>(Arrays.asList("test1", "test2", "test3"));
//        Status testStatus = new Status("This is a test post with stuff", "@FirstLast", datetime, testList, testList);
//        storyDAO.addStory(testStatus);
//
//        StatusListResponse response = storyDAO.getStory(new StatusListRequest(new AuthToken("test", "test"), "@irstLast", 25, null));
//
//        System.out.println(response.getHasMorePages());

        FollowDAO followDAO = (FollowDAO) daoFactory.create("FollowDAO");
        User jim = new User("Jim", "test", "@jim", null);
        User jeff = new User("Jeff", "test", "@jeff", null);
        User jake = new User("Jake", "test", "@jake", null);
        User alex = new User("Alex", "test", "@alex", null);
//        followDAO.followUser(jake, jim);
//        followDAO.followUser(jeff, jim);
//        followDAO.followUser(alex, jim);

        FollowListResponse response = followDAO.getFollowers("@jim", "@alex");
        System.out.println(followDAO.isFollower(jeff, jim));

    }
}
