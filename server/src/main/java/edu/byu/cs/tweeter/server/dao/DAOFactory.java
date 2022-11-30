package edu.byu.cs.tweeter.server.dao;

public class DAOFactory {
    public IDAO create(String daoType) {
        if ("AuthTokenDAO".equalsIgnoreCase(daoType)) {
            return new AuthTokenDAO();
        }
        else if ("FollowDAO".equalsIgnoreCase(daoType)) {
            return new FollowDAO();
        }
        else if ("StoryDAO".equalsIgnoreCase(daoType)) {
            return new StoryDAO();
        }
        else if ("UserDAO".equalsIgnoreCase(daoType)) {
            return new UserDAO();
        }

        return null;
    }
}
