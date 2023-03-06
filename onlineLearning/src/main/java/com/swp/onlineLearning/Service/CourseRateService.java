package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.CourseRateDTO;

import java.util.HashMap;

public interface CourseRateService {
    HashMap<String, Object> createCourseRate(CourseRateDTO courseRateDTO, String authority);
    HashMap<String, Object> getCourseRate(int courseID, int page, int limit);
}
