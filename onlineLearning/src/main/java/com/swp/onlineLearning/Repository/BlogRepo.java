package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepo extends JpaRepository<Blog,Integer> {
    Blog save(Blog blog);

    Blog findByBlogName(String name);
}
