package org.example.repository;

import org.example.dto.Course;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CourseRepository {

    private final DynamoDbTable<Course> courseTable;

    public CourseRepository(DynamoDbEnhancedClient enhancedClient,
                            @Value("${dynamodb.table.name:Courses}") String tableName) {
        this.courseTable = enhancedClient.table(tableName, TableSchema.fromBean(Course.class));
    }

    public void save(Course course) {
        courseTable.putItem(course);
    }

    public Optional<Course> findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return Optional.ofNullable(courseTable.getItem(key));
    }

    public List<Course> findAll() {
        return courseTable.scan().items().stream().collect(Collectors.toList());
    }

    public void deleteById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        courseTable.deleteItem(key);
    }
}
