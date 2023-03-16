package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.Comment;
import com.swp.onlineLearning.Model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, String> {
    @Query(nativeQuery = true, value = "Select * from comment where lessonid = ?1 and parentid is null order by create_date desc")
    Page<Comment> findFatherComByLesson(int lessonID, Pageable pageable);
    @Query(nativeQuery = true, value = "Select * from comment where blogid = ?1 and parentid is null order by create_date desc")
    Page<Comment> findFatherComByBlog(String blogID, Pageable pageable);
    List<Comment> findByParentIDAndLessonOrderByCreateDateDesc(Comment comment, Lesson lesson);
    List<Comment> findByParentIDAndBlogOrderByCreateDateDesc(Comment comment, Blog blog);
    Comment findByCommentID(String id);
    void deleteInBatch(Iterable<Comment> comments);
}
