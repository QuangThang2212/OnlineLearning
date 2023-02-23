package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.LessonPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonPackageRepo extends JpaRepository<LessonPackage,Integer> {
    List<LessonPackage> findByCourse(Course course);
}
