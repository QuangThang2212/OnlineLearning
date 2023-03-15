package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.DTO.CourseFilterObjectDTO;
import org.springframework.data.domain.Pageable;


public interface CourseFilterRepo {
    CourseFilterObjectDTO findCourseFilter(Integer typeFilter, String sort, Boolean kind, Pageable pageable);
}
