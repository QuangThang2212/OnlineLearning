package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.CourseTypeDTO;

import java.util.HashMap;

public interface CourseTypeService {
    HashMap<String, Object> save(CourseTypeDTO courseTypeDTO);
    HashMap<String, Object> update(CourseTypeDTO courseTypeDTO);
    HashMap<String, Object> delete(int id);
    HashMap<String, Object> findAll();
}
