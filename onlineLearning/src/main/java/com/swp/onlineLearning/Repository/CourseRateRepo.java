package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRateRepo extends JpaRepository<CourseRate, String> {
    CourseRate findByCourseAndAccount(Course course, Account account);
}
