package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseTypeRepo extends JpaRepository<CourseType, Integer> {
    void delete(CourseType courseType);
    CourseType findByCourseTypeName(String name);
    CourseType findByCourseTypeID(int id);
    List<CourseType> findAll();


}
