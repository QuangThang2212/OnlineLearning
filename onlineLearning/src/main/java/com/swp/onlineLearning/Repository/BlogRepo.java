package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Account;
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
public interface BlogRepo extends JpaRepository<Blog,String> {
    Blog findByBlogName(String BlogName);
    List<Blog> findByCourseType(CourseType courseType);
    @Query(nativeQuery = true, value = "select * from blog order by create_date desc")
    Page<Blog> findAll(Pageable pageable);
    @Query(nativeQuery = true, value = "select * from blog where blog_name like :name% order by create_date desc")
    Page<Blog> searchByName(Pageable pageable,@Param("name")String name);
    Blog findByBlogID(String id);
    List<Blog> findByAccount(Account account);
    @Query(nativeQuery = true, value = "select * from blog where blog_name=?1 and blogid != ?2")
    Blog findByBlogNameForUpdate(String BlogName, String id);
    @Query(nativeQuery = true, value = "select * from blog a inner join blog_react b on a.blogid = b.blogid where b.accountid=?1")
    List<Blog> findFavoriteBlog(Integer accountid);
}
