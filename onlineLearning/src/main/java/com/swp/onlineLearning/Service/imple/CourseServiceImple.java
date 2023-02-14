package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.CourseDTO;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Repository.CourseRepo;
import com.swp.onlineLearning.Service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class CourseServiceImple implements CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Override
    public HashMap<String, Object> getHomepageInfor() {
        HashMap<String, Object> json = new HashMap<>();

        json.put("PopularCourse", courseRepo.findTop8PopularCourse());
        json.put("FreePopularCourse", courseRepo.findTop8FreePopularCourse());
        json.put("NewestCourse", courseRepo.findTop8NewestCourse());
        json.put("FamousPaidCourses", courseRepo.findTop8FamousPaidCourses());

        return json;
    }

    @Override
    public Course findById(String courseID) {
        return null;
    }

    @Override
    public HashMap<String, Object> save(CourseDTO courseDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);
        if(courseDTO == null ){
            log.error("Not allow pass object null");
            json.put("msg", "Not allow pass object null");
            return json;
        }

        return null;
    }

    @Override
    public Course update(Course course) {
        return null;
    }

    @Override
    public Course Delete(Course course) {
        return null;
    }
}
