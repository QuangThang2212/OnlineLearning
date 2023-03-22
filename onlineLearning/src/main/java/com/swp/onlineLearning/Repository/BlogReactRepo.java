package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.BlogReact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogReactRepo extends JpaRepository<BlogReact,Integer> {
    BlogReact findByBlogReactID(String id);
    Page<BlogReact> findAll(Pageable pageable);
}
