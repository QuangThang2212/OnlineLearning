package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.CourseTypeDTO;

import java.util.HashMap;
import java.util.Map;

public interface CourseTypeService {
    HashMap<String, Object> save(CourseTypeDTO courseTypeDTO);
    HashMap<String, Object> findAll();
}
