package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CourseRepo extends JpaRepository<Course, String> {
    List<Course> findAll();
    Optional<Course> findByCourseID(int courseID);
    List<Course> findByCourseType(CourseType courseType);
    Course save(Course course);
    void delete(Course course);
    @Query(nativeQuery = true, value = "Select * From Course Order By NumberOfEnroll DESC LIMIT 8")
    List<Course> findTop8PopularCourse();

    @Query(nativeQuery = true, value = "Select * From Course Order By CreateDate ASC LIMIT 8")
    List<Course> findTop8NewestCourse();
    @Query(nativeQuery = true, value = "Select * From Course Where Price=0 Order By NumberOfEnroll DESC LIMIT 8")
    List<Course> findTop8FreePopularCourse();
    @Query(nativeQuery = true, value = "Select * From Course Where Price!=0 Order By NumberOfEnroll DESC LIMIT 8")
    List<Course> findTop8FamousPaidCourses();
}
