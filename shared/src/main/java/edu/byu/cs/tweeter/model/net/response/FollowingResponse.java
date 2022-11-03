package edu.byu.cs.tweeter.model.net.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;

/**
 * A paged response for a {@link FollowingRequest}.
 */
public class FollowingResponse extends PagedResponse {

    private List<User> followees;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowingResponse(String message) {
        super(false, message, false);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param followees the followees to be included in the result.
     * @param hasMorePages an indicator of whether more data is available for the request.
     */
    public FollowingResponse(List<User> followees, boolean hasMorePages) {
        super(true, hasMorePages);
        this.followees = followees;
    }

    /**
     * Returns the followees for the corresponding request.
     *
     * @return the followees.
     */
    public List<User> getFollowees() {
        return followees;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        FollowingResponse that = (FollowingResponse) param;

        return (Objects.equals(followees, that.followees) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(followees);
    }
}
