package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseTypeRepo extends JpaRepository<CourseType, Integer> {
    void delete(CourseType courseType);
    @Query(nativeQuery = true, value = "SELECT * FROM course_type a where course_type_name=?1 and course_typeid != ?2")
    CourseType findByCourseTypeNameAndID(String name, int id);
    CourseType findByCourseTypeName(String name);
    CourseType findByCourseTypeID(int id);
    List<CourseType> findAll();


}
