package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.CourseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepo extends JpaRepository<Blog,Integer> {
    Blog findByBlogName(String BlogName);
    List<Blog> findByCourseType(CourseType courseType);
    Page<Blog> findAll(Pageable pageable);
    @Query(nativeQuery = true, value = "select * from blog where blog_name like :name%")
    Page<Blog> searchByName(Pageable pageable,@Param("name")String name);
    Blog findByBlogID(String id);

}
