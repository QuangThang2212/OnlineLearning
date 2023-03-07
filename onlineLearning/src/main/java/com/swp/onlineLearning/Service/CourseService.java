package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.EnrollInformationDTO;
import com.swp.onlineLearning.DTO.ListOfCourseDTO;
import com.swp.onlineLearning.DTO.CourseDTO;
import com.swp.onlineLearning.DTO.ListOfPackageDTO;

import java.util.HashMap;

public interface CourseService {
    HashMap<String, Object> getHomepageInfor(String authority);
    HashMap<String, Object> save(CourseDTO courseDTO);
    HashMap<String, Object> delete(Integer id);
    HashMap<String, Object> saveLessonPackage(ListOfPackageDTO listOfPackageDTO, int id);
    HashMap<String, Object> changeCourseStatus(ListOfCourseDTO listOfCourseDTO);
    HashMap<String, Object> findAll(int page, int size, String role);
    HashMap<String, Object> findCourseByIdToUpdate(Integer id);
    HashMap<String, Object> findAllPurchaseCourse(int page, int size, String search);
    HashMap<String, Object> findCourseById(String authority, Integer id);
    HashMap<String, Object> enrollCourse(String authority, EnrollInformationDTO enrollInformationDTO);
}
