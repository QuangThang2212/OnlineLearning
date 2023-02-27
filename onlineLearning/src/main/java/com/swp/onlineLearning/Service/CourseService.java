package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.ListOfCourseDTO;
import com.swp.onlineLearning.DTO.CourseDTO;
import com.swp.onlineLearning.DTO.ListOfPackageDTO;

import java.util.HashMap;

public interface CourseService {
    HashMap<String, Object> getHomepageInfor();
    HashMap<String, Object> save(CourseDTO courseDTO);
    HashMap<String, Object> saveLessonPackage(ListOfPackageDTO listOfPackageDTO, int id);
    HashMap<String, Object> changeCourseStatus(ListOfCourseDTO listOfCourseDTO);
    HashMap<String, Object> findAll(int page, int size, String role);
    HashMap<String, Object> findCourseByIdToUpdate(Integer id);
}
