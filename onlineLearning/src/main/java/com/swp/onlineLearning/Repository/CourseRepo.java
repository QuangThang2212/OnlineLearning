package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CourseRepo extends JpaRepository<Course, Integer> {
    Page<Course> findAll(Pageable pageable);
    Course findByCourseID(int courseID);
    @Query(nativeQuery = true, value = "SELECT * FROM course a where course_name=?1 and courseid != ?2")
    Course findByCourseNameAndID(String name, int course);
    Course findByCourseName(String name);
    List<Course> findByCourseType(CourseType courseType);
    void delete(Course course);
    @Query(nativeQuery = true, value = "Select * From Course Order By number_of_enroll DESC LIMIT 8")
    List<Course> findTop8PopularCourse();

    @Query(nativeQuery = true, value = "Select * From Course Order By create_date ASC LIMIT 8")
    List<Course> findTop8NewestCourse();
    @Query(nativeQuery = true, value = "Select * From Course Where Price=0 Order By number_of_enroll DESC LIMIT 8")
    List<Course> findTop8FreePopularCourse();
    @Query(nativeQuery = true, value = "Select * From Course Where Price!=0 Order By number_of_enroll DESC LIMIT 8")
    List<Course> findTop8FamousPaidCourses();

}
