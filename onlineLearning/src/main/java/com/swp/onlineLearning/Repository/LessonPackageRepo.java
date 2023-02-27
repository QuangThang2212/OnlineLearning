package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.Lesson;
import com.swp.onlineLearning.Model.LessonPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonPackageRepo extends JpaRepository<LessonPackage,Integer> {
    List<LessonPackage> findByCourse(Course course);
    LessonPackage findByPackageID(int id);
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM lesson_package l WHERE l.packageid = ?1")
    void deleteByLessonPackageID(int id);
}
