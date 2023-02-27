package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepo extends JpaRepository<Answer, Integer> {
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM answer l WHERE l.answerid = ?1")
    void deleteByAnswerID(int id);
}
