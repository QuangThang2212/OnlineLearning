package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.Comment;
import com.swp.onlineLearning.Model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, String> {
    @Query(nativeQuery = true, value = "Select * from comment where lessonid = ?1 and parentid is null")
    List<Comment> findFatherComByLesson(int lessonID);
    @Query(nativeQuery = true, value = "Select * from comment where blogid = ?1 and parentid is null")
    List<Comment> findFatherComByBlog(String blogID);
    List<Comment> findByParentIDAndLesson(Comment comment, Lesson lesson);
    List<Comment> findByParentIDAndBlog(Comment comment, Blog blog);
}
