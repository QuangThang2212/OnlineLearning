package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Lesson;
import com.swp.onlineLearning.Model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizResultRepo extends JpaRepository<QuizResult, String> {
    QuizResult findByAccountAndLesson(Account account, Lesson lesson);
    @Query(nativeQuery = true, value = """
            SELECT a.* FROM quiz_result a\s
            inner join lesson b on a.lessonid=b.lessonid\s
            inner join lesson_package c on b.packageid=c.packageid
            where a.accountid=?1 and c.courseid=?2 and a.status=true""")
    List<QuizResult> findAllPassedQuizOfUser(int accountID, int courseID);
}
