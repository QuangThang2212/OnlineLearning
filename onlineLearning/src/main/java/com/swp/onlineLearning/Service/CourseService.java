package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.CourseDTO;
import com.swp.onlineLearning.Model.Course;

import java.util.HashMap;

public interface CourseService {
    HashMap<String, Object> getHomepageInfor();
    HashMap<String, Object> save(CourseDTO courseDTO);
    Course Delete (Course course);
}
