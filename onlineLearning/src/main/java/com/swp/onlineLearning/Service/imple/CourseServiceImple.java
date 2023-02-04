package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.HomePageObject;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Repository.CourseRepo;
import com.swp.onlineLearning.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CourseServiceImple implements CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Override
    public HomePageObject getHomepageInfor() {
        HomePageObject homePageObject = new HomePageObject();
        return homePageObject;
    }

    @Override
    public Course findById(String courseID) {
        Optional<Course> course = courseRepo.findById(courseID);
        return course.orElse(null);
    }

    @Override
    public Course save(Course course) {
        return null;
    }

    @Override
    public Course update(Course course) {
        return null;
    }

    @Override
    public Course Delete(Course course) {
        return null;
    }
}
