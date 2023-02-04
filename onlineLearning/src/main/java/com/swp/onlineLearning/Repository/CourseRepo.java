package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CourseRepo extends JpaRepository<Course, String> {
    @Override
    List<Course> findAll();
    @Override
    Optional<Course> findById(String courseID);
    Course save(Course course);
    void delete(Course course);
}
