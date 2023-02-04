package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.HomePageObject;
import com.swp.onlineLearning.Model.Course;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CourseService {
    HomePageObject getHomepageInfor();
    Course findById(String courseID);
    Course save(Course course);
    Course update (Course course);
    Course Delete (Course course);
}
