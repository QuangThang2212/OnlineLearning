package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.LessonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonTypeRepo extends JpaRepository<LessonType, Integer> {
    LessonType findByName (String name);
}
