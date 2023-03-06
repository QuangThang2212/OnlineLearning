package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRateRepo extends JpaRepository<CourseRate, String> {
    CourseRate findByCourseAndAccount(Course course, Account account);
    @Query(nativeQuery = true, value = "select * from course_rate where courseid=?1 and content is not null and star_rate!=0")
    List<CourseRate> findByCourse(int courseid);
    @Query(nativeQuery = true, value = "select * from course_rate where courseid=?1 and content is not null and star_rate!=0")
    Page<CourseRate> findByCourse(int courseid, Pageable pageable);
}
