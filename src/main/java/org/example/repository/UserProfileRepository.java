package org.example.repository;

import org.example.dto.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class UserProfileRepository {

    private final DynamoDbTable<UserProfile> profileTable;

    @Autowired
    public UserProfileRepository(DynamoDbEnhancedClient enhancedClient) {
        this.profileTable = enhancedClient.table("UserProfiles", TableSchema.fromBean(UserProfile.class));
    }

    public UserProfile save(UserProfile profile) {
        profileTable.putItem(profile);
        return profile;
    }

    public UserProfile findById(String userId) {
        Key key = Key.builder().partitionValue(userId).build();
        return profileTable.getItem(key);
    }
}
