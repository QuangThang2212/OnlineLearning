package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.Lesson;
import com.swp.onlineLearning.Model.LessonPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface LessonRepo extends JpaRepository<Lesson, String> {
    Optional<Lesson> findByNameAndLessonPackage(String name, LessonPackage lessonPackage);
    List<Lesson> findByLessonPackage(LessonPackage lessonPackage);
}
