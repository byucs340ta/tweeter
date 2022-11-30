package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import edu.byu.cs.tweeter.model.domain.Follower;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.net.request.FollowListRequest;
import edu.byu.cs.tweeter.model.net.response.FollowListResponse;
import edu.byu.cs.tweeter.model.net.response.SuccessResponse;
import edu.byu.cs.tweeter.util.FakeData;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowDAO implements IFollowDAO {
    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion(Regions.US_EAST_1)
            .build();
    private final DynamoDB dynamoDB = new DynamoDB(client);
    private Table followTable = dynamoDB.getTable("340_tweeter_follows");
    @Override
    public void followUser(User follower, User followee) {
        Item item = new Item().withPrimaryKey("follower_handle", follower.getAlias(), "followee_handle", followee.getAlias())
                .withString("follower_first_name", follower.getFirstName())
                .withString("follower_last_name", follower.getLastName())
                .withString("followee_first_name", followee.getFirstName())
                .withString("followee_last_name", followee.getLastName());

        followTable.putItem(item);
    }

    @Override
    public void unfollowUser(User follower, User followee) {
        DeleteItemSpec spec = new DeleteItemSpec().withPrimaryKey("follower_handle", follower.getAlias(), "followee_handle", followee.getAlias());
        followTable.deleteItem(spec);
    }

    @Override
    public boolean isFollower(User follower, User followee) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("follower_handle", follower.getAlias(), "followee_handle", followee.getAlias());
        Item item = followTable.getItem(spec);
        return item != null;
    }

    @Override
    public FollowListResponse getFollowers(String user, String lastItem) {
        QuerySpec spec = new QuerySpec().withKeyConditionExpression("followee_handle = :alias")
                .withValueMap(new ValueMap().withString(":alias", user))
                .withMaxResultSize(25);

        if (lastItem != null) {
            spec.withExclusiveStartKey("followee_handle", user, "follower_handle", lastItem);
        }

        Index followsIndex = followTable.getIndex("follows_index");
        ItemCollection<QueryOutcome> itemCollection = followsIndex.query(spec);
        Iterator<Item> itemIterator = itemCollection.iterator();
        ArrayList<Follower> followers = new ArrayList<>();
        while (itemIterator.hasNext()) {
            Item item = itemIterator.next();
            String firstName = item.getString("follower_first_name");
            String lastName = item.getString("follower_last_name");
            String alias = item.getString("follower_handle");
            Follower follower = new Follower(firstName, lastName, alias);
            followers.add(follower);
        }

        boolean hasMorePages = itemCollection.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null;
        return new FollowListResponse(followers, hasMorePages);
    }

    @Override
    public FollowListResponse getFollowees(String user, String lastItem) {
        QuerySpec spec = new QuerySpec().withKeyConditionExpression("follower_handle = :alias")
                .withValueMap(new ValueMap().withString(":alias", user))
                .withMaxResultSize(25);

        if (lastItem != null) {
            spec.withExclusiveStartKey("follower_handle", user, "followee_handle", lastItem);
        }

        ItemCollection<QueryOutcome> itemCollection = followTable.query(spec);
        Iterator<Item> itemIterator = itemCollection.iterator();
        ArrayList<Follower> followers = new ArrayList<>();
        while (itemIterator.hasNext()) {
            Item item = itemIterator.next();
            String firstName = item.getString("followee_first_name");
            String lastName = item.getString("followee_last_name");
            String alias = item.getString("followee_handle");
            Follower follower = new Follower(firstName, lastName, alias);
            followers.add(follower);
        }

        boolean hasMorePages = itemCollection.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null;
        return new FollowListResponse(followers, hasMorePages);
    }

//    /**
//     * Gets the count of users from the database that the user specified is following. The
//     * current implementation uses generated data and doesn't actually access a database.
//     *
//     * @param follower the User whose count of how many following is desired.
//     * @return said count.
//     */
//    public Integer getFolloweeCount(User follower) {
//        // TODO: uses the dummy data.  Replace with a real implementation.
//        assert follower != null;
//        return getDummyFollowees().size();
//    }
//
//    public Integer getFollowerCount(User user) {
//        assert user != null;
//        return getDummyFollowers().size();
//    }
//
//    /**
//     * Gets the users from the database that the user specified in the request is following. Uses
//     * information in the request object to limit the number of followees returned and to return the
//     * next set of followees after any that were returned in a previous request. The current
//     * implementation returns generated data and doesn't actually access a database.
//     *
//     * @param request contains information about the user whose followees are to be returned and any
//     *                other information required to satisfy the request.
//     * @return the followees.
//     */
//    public FollowListResponse getFollowees(FollowListRequest request) {
//        // TODO: Generates dummy data. Replace with a real implementation.
//        assert request.getLimit() > 0;
//        assert request.getTargetUser() != null;
//
//        List<User> allFollowees = getDummyFollowees();
//        List<User> responseFollowees = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if(request.getLimit() > 0) {
//            if (allFollowees != null) {
//                int followeesIndex = getFolloweesStartingIndex(request.getLastFolloweeAlias(), allFollowees);
//
//                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
//                    responseFollowees.add(allFollowees.get(followeesIndex));
//                }
//
//                hasMorePages = followeesIndex < allFollowees.size();
//            }
//        }
//
//        return new FollowListResponse(responseFollowees, hasMorePages);
//    }
//
//    public FollowListResponse getFollowers(FollowListRequest request) {
//        // TODO: Generates dummy data. Replace with a real implementation.
//        assert request.getLimit() > 0;
//        assert request.getTargetUser() != null;
//
//        List<User> allFollowees = getDummyFollowees();
//        List<User> responseFollowees = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if(request.getLimit() > 0) {
//            if (allFollowees != null) {
//                int followeesIndex = getFolloweesStartingIndex(request.getLastFolloweeAlias(), allFollowees);
//
//                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
//                    responseFollowees.add(allFollowees.get(followeesIndex));
//                }
//
//                hasMorePages = followeesIndex < allFollowees.size();
//            }
//        }
//
//        return new FollowListResponse(responseFollowees, hasMorePages);
//    }
//
//    /**
//     * Determines the index for the first followee in the specified 'allFollowees' list that should
//     * be returned in the current request. This will be the index of the next followee after the
//     * specified 'lastFollowee'.
//     *
//     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
//     *                          request or null if there was no previous request.
//     * @param allFollowees the generated list of followees from which we are returning paged results.
//     * @return the index of the first followee to be returned.
//     */
//    private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {
//
//        int followeesIndex = 0;
//
//        if(lastFolloweeAlias != null) {
//            // This is a paged request for something after the first page. Find the first item
//            // we should return
//            for (int i = 0; i < allFollowees.size(); i++) {
//                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
//                    // We found the index of the last item returned last time. Increment to get
//                    // to the first one we should return
//                    followeesIndex = i + 1;
//                    break;
//                }
//            }
//        }
//
//        return followeesIndex;
//    }
//
//    public SuccessResponse followUser(FollowUserRequest request) {
//        return new SuccessResponse(true);
//    }
//
//    /**
//     * Returns the list of dummy followee data. This is written as a separate method to allow
//     * mocking of the followees.
//     *
//     * @return the followees.
//     */
//    List<User> getDummyFollowees() {
//        return getFakeData().getFakeUsers();
//    }
//
//    List<User> getDummyFollowers() {
//        return getFakeData().getFakeUsers();
//    }
//
//    /**
//     * Returns the {@link FakeData} object used to generate dummy followees.
//     * This is written as a separate method to allow mocking of the {@link FakeData}.
//     *
//     * @return a {@link FakeData} instance.
//     */
//    FakeData getFakeData() {
//        return FakeData.getInstance();
//    }
}
