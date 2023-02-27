package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<Question,Integer> {
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM question l WHERE l.questionid = ?1")
    void deleteByQuestionID(int id);
}
