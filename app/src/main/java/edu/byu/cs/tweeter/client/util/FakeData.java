package edu.byu.cs.tweeter.client.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import edu.byu.cs.tweeter.shared.domain.AuthToken;
import edu.byu.cs.tweeter.shared.domain.Status;
import edu.byu.cs.tweeter.shared.domain.User;

/**
 * Generates fake (or "dummy") data for Milestone 2.
 * Since the client is not yet integrated with a server, we need to generate some fake
 * data to demonstrate the app's functionality.
 */
public class FakeData {

    /**
     * Test user profile images.
     */
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    /**
     * Generated users.
     */
    private static final User user1 = new User("Allen", "Anderson", "@allen", MALE_IMAGE_URL);
    private static final User user2 = new User("Amy", "Ames", "@amy", FEMALE_IMAGE_URL);
    private static final User user3 = new User("Bob", "Bobson", "@bob", MALE_IMAGE_URL);
    private static final User user4 = new User("Bonnie", "Beatty", "@bonnie", FEMALE_IMAGE_URL);
    private static final User user5 = new User("Chris", "Colston", "@chris", MALE_IMAGE_URL);
    private static final User user6 = new User("Cindy", "Coats", "@cindy", FEMALE_IMAGE_URL);
    private static final User user7 = new User("Dan", "Donaldson", "@dan", MALE_IMAGE_URL);
    private static final User user8 = new User("Dee", "Dempsey", "@dee", FEMALE_IMAGE_URL);
    private static final User user9 = new User("Elliott", "Enderson", "@elliott", MALE_IMAGE_URL);
    private static final User user10 = new User("Elizabeth", "Engle", "@elizabeth", FEMALE_IMAGE_URL);
    private static final User user11 = new User("Frank", "Frandson", "@frank", MALE_IMAGE_URL);
    private static final User user12 = new User("Fran", "Franklin", "@fran", FEMALE_IMAGE_URL);
    private static final User user13 = new User("Gary", "Gilbert", "@gary", MALE_IMAGE_URL);
    private static final User user14 = new User("Giovanna", "Giles", "@giovanna", FEMALE_IMAGE_URL);
    private static final User user15 = new User("Henry", "Henderson", "@henry", MALE_IMAGE_URL);
    private static final User user16 = new User("Helen", "Hopwell", "@helen", FEMALE_IMAGE_URL);
    private static final User user17 = new User("Igor", "Isaacson", "@igor", MALE_IMAGE_URL);
    private static final User user18 = new User("Isabel", "Isaacson", "@isabel", FEMALE_IMAGE_URL);
    private static final User user19 = new User("Justin", "Jones", "@justin", MALE_IMAGE_URL);
    private static final User user20 = new User("Jill", "Johnson", "@jill", FEMALE_IMAGE_URL);
    private static final User user21 = new User("John", "Brown", "@john", MALE_IMAGE_URL);

    /**
     * Generated auth token.
     */
    private static final AuthToken authToken = new AuthToken();

    /**
     * List of generated users.
     * Used to return lists of followers and followees.
     */
    private static final List<User> allUsers = Arrays.asList(
            user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11,
            user12, user13, user14, user15, user16, user17, user18, user19, user20, user21
    );

    /**
     * List of generated statuses.
     * Used to return lists of story and feed statuses.
     */
    private static final List<Status> allStatus = new ArrayList<>();

    static {
        // Generate fake statuses

        Calendar calendar = new GregorianCalendar();

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < allUsers.size(); ++j) {
                User sender = allUsers.get(j);
                User mention = ((j < allUsers.size() - 1) ? allUsers.get(j + 1) : allUsers.get(0));
                List<String> mentions = Arrays.asList(mention.getAlias());
                String url = "https://byu.edu";
                List<String> urls = Arrays.asList(url);
                String post = "Post " + i + " " + j +
                        "\nMy friend " + mention.getAlias() + " likes this website" +
                        "\n" + url;
                calendar.add(Calendar.MINUTE, 1);
                String datetime = calendar.getTime().toString();
                Status status = new Status(post, sender, datetime, urls, mentions);
                allStatus.add(status);
            }
        }
    }

    public static User getFirstUser() {
        return user1;
    }

    public static AuthToken getAuthToken() {
        return authToken;
    }

    public static User findUserByAlias(String alias) {
        for (User u : allUsers) {
            if (u.getAlias().equals(alias)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Returns a page of users (followers or followees)
     *
     * @param lastUser the last user returned in the previous page of results.
     * @param limit    maximum number of users to return (i.e., page size).
     * @param omit     if not null, specifies a user that should not be returned.
     * @return a Pair containing a page of users and a "hasMorePages" flag.
     */
    public static Pair<List<User>, Boolean> getPageOfUsers(User lastUser, int limit, User omit) {

        Pair<List<User>, Boolean> result = new Pair<>(new ArrayList<User>(), false);

        int index = 0;

        if (lastUser != null) {
            for (int i = 0; i < allUsers.size(); ++i) {
                User curUser = allUsers.get(i);
                if (curUser.getAlias().equals(lastUser.getAlias())) {
                    index = i + 1;
                    break;
                }
            }
        }

        for (int count = 0; index < allUsers.size() && count < limit; ++count, ++index) {
            User curUser = allUsers.get(index);
            if (omit == null || !curUser.getAlias().equals(omit.getAlias())) {
                result.getFirst().add(curUser);
            }
        }

        result.setSecond(index < allUsers.size());

        return result;
    }


    /**
     * Returns a page of statuses (story or feed)
     *
     * @param lastStatus the last status returned in the previous page of results.
     * @param limit      maximum number of statuses to return (i.e., page size).
     * @return a Pair containing a page of statuses and a "hasMorePages" flag.
     */
    public static Pair<List<Status>, Boolean> getPageOfStatus(Status lastStatus, int limit) {

        Pair<List<Status>, Boolean> result = new Pair<>(new ArrayList<Status>(), false);

        int index = 0;

        if (lastStatus != null) {
            for (int i = 0; i < allStatus.size(); ++i) {
                Status curStatus = allStatus.get(i);
                if (curStatus.getUser().getAlias().equals(lastStatus.getUser().getAlias()) &&
                        curStatus.getDate().equals(lastStatus.getDate())) {
                    index = i + 1;
                    break;
                }
            }
        }

        for (int count = 0; index < allStatus.size() && count < limit; ++count, ++index) {
            Status curStatus = allStatus.get(index);
            result.getFirst().add(curStatus);
        }

        result.setSecond(index < allStatus.size());

        return result;
    }

}
