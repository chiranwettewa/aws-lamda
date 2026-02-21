package org.example.dto;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDate;

@DynamoDbBean
public class Task {
    private String userId;
    private String id;
    private String title;
    private String description;
    private String category;
    private String location;
    private String phoneNumber;
    private String taskType;
    private Double budget;
    private String budgetType;
    private String dueDate;
    private String status;
    private Long createdAt;
    private Long updatedAt;

    public Task() {
    }

    @DynamoDbPartitionKey
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    @DynamoDbSortKey
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }

    public String getBudgetType() { return budgetType; }
    public void setBudgetType(String budgetType) { this.budgetType = budgetType; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }

    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
}
