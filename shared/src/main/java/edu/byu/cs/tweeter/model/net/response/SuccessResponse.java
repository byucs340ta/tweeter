package edu.byu.cs.tweeter.model.net.response;

public class SuccessResponse extends Response {
    public SuccessResponse(boolean success, String message) {
        super(success, message);
    }

    public SuccessResponse(boolean success) {
        super(success);
    }

}
