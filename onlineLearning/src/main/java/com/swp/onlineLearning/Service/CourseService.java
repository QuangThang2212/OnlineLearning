package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.CourseDTO;
import com.swp.onlineLearning.Model.Course;

import java.util.HashMap;

public interface CourseService {
    HashMap<String, Object> getHomepageInfor();
    Course findById(String courseID);
    HashMap<String, Object> save(CourseDTO courseDTO);
    Course update (Course course);
    Course Delete (Course course);
}
