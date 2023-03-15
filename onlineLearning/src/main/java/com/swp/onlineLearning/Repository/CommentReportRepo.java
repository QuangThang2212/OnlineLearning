package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Comment;
import com.swp.onlineLearning.Model.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReportRepo extends JpaRepository<CommentReport, Integer> {
    CommentReport findByCommentAndAccount(Comment comment, Account account);
    void deleteInBatch(Iterable<CommentReport> comments);
}
