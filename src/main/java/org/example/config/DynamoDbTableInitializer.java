package org.example.config;

import org.example.dto.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

@Component
public class DynamoDbTableInitializer implements CommandLineRunner {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Override
    public void run(String... args) {
        try {
            DynamoDbTable<Task> taskTable = enhancedClient.table("Tasks", TableSchema.fromBean(Task.class));
            taskTable.createTable();
            System.out.println("Tasks table created successfully");
        } catch (ResourceInUseException e) {
            System.out.println("Tasks table already exists");
        } catch (Exception e) {
            System.err.println("Error creating Tasks table: " + e.getMessage());
        }
    }
}
