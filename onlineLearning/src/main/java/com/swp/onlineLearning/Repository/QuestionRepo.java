package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<Question,Integer> {
    void deleteInBatch(Iterable<Question> questions);
}
