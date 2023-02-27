package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepo extends JpaRepository<Answer, Integer> {
    void deleteInBatch(Iterable<Answer> answers);
}
