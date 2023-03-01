package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Lesson;
import com.swp.onlineLearning.Model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizResultRepo extends JpaRepository<QuizResult, String> {
    QuizResult findByAccountAndLesson(Account account, Lesson lesson);
}
