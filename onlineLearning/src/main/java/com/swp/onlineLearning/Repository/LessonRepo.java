package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Lesson;
import com.swp.onlineLearning.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LessonRepo extends JpaRepository<Lesson, Integer> {
    Lesson findByLessonID(int id);
    void deleteInBatch(Iterable<Lesson> lessons);
}
