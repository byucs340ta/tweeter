package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends  Response {
    private boolean isFollower;

    public boolean isFollower() {
        return isFollower;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
    }

    public IsFollowerResponse(boolean isFollower) {
        super(true, null);
        this.isFollower = isFollower;
    }
}
