package org.example.service;

import org.example.dto.Course;
import org.example.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }

    public boolean updateCourse(String id, Course newCourse) {
        if (courseRepository.findById(id).isPresent()) {
            newCourse.setId(id);
            courseRepository.save(newCourse);
            return true;
        }
        return false;
    }

    public boolean deleteCourse(String id) {
        if (courseRepository.findById(id).isPresent()) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}