package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepo extends JpaRepository<Lesson, Integer> {
    Lesson findByLessonID(int id);
    void deleteInBatch(Iterable<Lesson> lessons);
}
