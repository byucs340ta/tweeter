package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import edu.byu.cs.tweeter.model.domain.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDAO implements IUserDAO{

    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion(Regions.US_EAST_1)
            .build();
    private final DynamoDB dynamoDB = new DynamoDB(client);
    private Table userTable = dynamoDB.getTable("340_tweeter_user");

    @Override
    public void createUser(User user, String password) {
        String hashedPassword = hashPassword(password);
        Item item = new Item().withPrimaryKey("alias", user.getAlias())
                .withString("first_name", user.getFirstName())
                .withString("last_name", user.getLastName())
                .withString("image_url", user.getImageUrl())
                .withString("password", hashedPassword)
                .withNumber("follower_count", 0)
                .withNumber("followee_count", 0);
        userTable.putItem(item);
    }

    @Override
    public User getUser(String alias) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);
        Item item = userTable.getItem(spec);
        return new User(item.getString("first_name"), item.getString("last_name"), item.getString("alias"), item.getString("image_url"));
    }

    @Override
    public User login(String alias, String password) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);
        Item item = userTable.getItem(spec);
        String hashedPassword = hashPassword(password);

        if (!hashedPassword.equals(item.getString("password"))) {
            return null;
        }
        return new User(item.getString("first_name"), item.getString("last_name"), item.getString("alias"), item.getString("image_url"));
    }

    @Override
    public void addFollowerCount(String alias) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);
        Item item = userTable.getItem(spec);
        UpdateItemSpec update = new UpdateItemSpec().withPrimaryKey("alias", alias)
                .withUpdateExpression("set follower_count = :val")
                .withValueMap(new ValueMap().withNumber(":val", item.getInt("follower_count") + 1));
        userTable.updateItem(update);
    }

    @Override
    public void subtractFollowerCount(String alias) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);
        Item item = userTable.getItem(spec);
        UpdateItemSpec update = new UpdateItemSpec().withPrimaryKey("alias", alias)
                .withUpdateExpression("set follower_count = :val")
                .withValueMap(new ValueMap().withNumber(":val", item.getInt("follower_count") - 1));
        userTable.updateItem(update);
    }

    @Override
    public void addFolloweeCount(String alias) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);
        Item item = userTable.getItem(spec);
        UpdateItemSpec update = new UpdateItemSpec().withPrimaryKey("alias", alias)
                .withUpdateExpression("set followee_count = :val")
                .withValueMap(new ValueMap().withNumber(":val", item.getInt("followee_count") + 1));
        userTable.updateItem(update);
    }

    @Override
    public void subtractFolloweeCount(String alias) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);
        Item item = userTable.getItem(spec);
        UpdateItemSpec update = new UpdateItemSpec().withPrimaryKey("alias", alias)
                .withUpdateExpression("set followee_count = :val")
                .withValueMap(new ValueMap().withNumber(":val", item.getInt("followee_count") - 1));
        userTable.updateItem(update);
    }

    @Override
    public int getFollowerCount(String alias) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);
        Item item = userTable.getItem(spec);
        return item.getInt("follower_count");
    }

    @Override
    public int getFolloweeCount(String alias) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);
        Item item = userTable.getItem(spec);
        return item.getInt("followee_count");
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "hashing failed";
    }
}
