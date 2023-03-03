package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.CourseRateDTO;

import java.util.HashMap;

public interface CommentService {
    HashMap<String, Object> createCourseRate(CourseRateDTO courseRateDTO, String authority);
}
