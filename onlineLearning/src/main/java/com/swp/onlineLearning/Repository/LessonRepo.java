package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Lesson;
import com.swp.onlineLearning.Model.LessonPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LessonRepo extends JpaRepository<Lesson, Integer> {
}
