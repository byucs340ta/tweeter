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
import com.amazonaws.services.dynamodbv2.model.Update;
import edu.byu.cs.tweeter.model.domain.AuthToken;

import java.text.DateFormat;
import java.util.Date;

public class AuthTokenDAO implements  IAuthTokenDAO {
    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion(Regions.US_EAST_1)
            .build();
    private final DynamoDB dynamoDB = new DynamoDB(client);
    private Table authTokenTable = dynamoDB.getTable("340_tweeter_auth_token");
    @Override
    public void createAuthToken(AuthToken token) {
        Item item = new Item().withPrimaryKey("token", token.getToken())
                .withString("alias", token.getAlias())
                .withNumber("dt", new Date().getTime() + 300000); // 5 minute timeout
        authTokenTable.putItem(item);
    }

    @Override
    public AuthToken getAuthToken(String token) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("token", token);
        Item item = authTokenTable.getItem(spec);
        String datetime = dateFormat.format(item.getLong("dt"));
        return new AuthToken(item.getString("alias"), datetime);
    }

    @Override
    public void updateAuthToken(String token) {
        UpdateItemSpec spec = new UpdateItemSpec().withPrimaryKey("token", token)
                .withUpdateExpression("set dt = :val")
                .withValueMap(new ValueMap().withNumber(":val", new Date().getTime() + 300000));
        authTokenTable.updateItem(spec);
    }
}
