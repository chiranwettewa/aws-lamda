package org.example.repository;

import org.example.dto.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {

    private final DynamoDbTable<Task> taskTable;

    @Autowired
    public TaskRepository(DynamoDbEnhancedClient enhancedClient) {
        this.taskTable = enhancedClient.table("Tasks", TableSchema.fromBean(Task.class));
    }

    public Task save(Task task) {
        taskTable.putItem(task);
        return task;
    }

    public Task findById(String userId, String id) {
        Key key = Key.builder()
                .partitionValue(userId)
                .sortValue(id)
                .build();
        return taskTable.getItem(key);
    }

    public List<Task> findByUserId(String userId) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(userId).build());
        
        return taskTable.query(queryConditional)
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public List<Task> findAll() {
        return taskTable.scan()
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public void delete(String userId, String id) {
        Key key = Key.builder()
                .partitionValue(userId)
                .sortValue(id)
                .build();
        taskTable.deleteItem(key);
    }
}
