package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.LessonPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonPackageRepo extends JpaRepository<LessonPackage,Integer> {
    Optional<LessonPackage> findByNameAndCourse(String name, Course course);
    List<LessonPackage> findByCourse(Course course);
}
