package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.StatusListRequest;
import edu.byu.cs.tweeter.model.net.response.StatusListResponse;
import edu.byu.cs.tweeter.util.FakeData;

import java.util.ArrayList;
import java.util.List;

public class FeedDAO {
    public StatusListResponse getFeed(StatusListRequest request) {
        assert request.getLimit() > 0;
        assert  request.getUserAlias() != null;

        List<Status> feed = FakeData.getInstance().getFakeStatuses();
        List<Status> responseFeed = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            if (feed != null) {
                int statusIndex = getFeedStartingIndex(request.getLastStatus(), feed);

                for (int limitCounter = 0; statusIndex < feed.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                    responseFeed.add(feed.get(statusIndex));
                }

                hasMorePages = statusIndex < feed.size();
            }
        }

        return new StatusListResponse(responseFeed, hasMorePages);
    }

    private int getFeedStartingIndex(Status lastStatus, List<Status> allStatus) {
        int statusIndex = 0;

        if (lastStatus != null) {
            for (int i = 0; i < allStatus.size(); i++) {
                if (lastStatus.equals(allStatus.get(i))) {
                    statusIndex = i + 1;
                    break;
                }
            }
        }

        return statusIndex;
    }
}
