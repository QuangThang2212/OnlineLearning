package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.CourseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepo extends JpaRepository<Blog,Integer> {
    Blog findByBlogName(String BlogName);
    List<Blog> findByCourseType(CourseType courseType);
    Page<Blog> findAll(Pageable pageable);
}