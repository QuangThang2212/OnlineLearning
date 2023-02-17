package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepo extends JpaRepository<Blog,Integer> {
    Blog save(Blog blog);
    Blog findByBlogName(String name);
    List<Blog> findByCourseType(CourseType courseType);
}
