package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface IAuthTokenDAO extends IDAO {
    void createAuthToken(AuthToken token);
    AuthToken getAuthToken(String token);
    void updateAuthToken(String token);
}
