package edu.byu.cs.tweeter.model.net.response;

public class FollowCountResponse extends Response {
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public FollowCountResponse(int count) {
        super(true, null);
        this.count = count;
    }
}
