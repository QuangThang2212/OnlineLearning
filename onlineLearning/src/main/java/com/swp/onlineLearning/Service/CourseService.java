package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.HomePageObject;
import com.swp.onlineLearning.Model.Course;

public interface CourseService {
    HomePageObject getHomepageInfor();
    Course findById(String courseID);
    Course save(Course course);
    Course update (Course course);
    Course Delete (Course course);
}
