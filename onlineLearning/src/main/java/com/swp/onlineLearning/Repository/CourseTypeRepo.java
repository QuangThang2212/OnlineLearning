package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseTypeRepo extends JpaRepository<CourseType, Integer> {
    CourseType save(CourseType courseType);
    CourseType findByCourseTypeName(String name);
    List<CourseType> findAll();
}
