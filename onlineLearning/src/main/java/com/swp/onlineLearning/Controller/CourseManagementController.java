package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.Model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseManagementController {
    @PostMapping("/create")
    public ResponseEntity<Course> createNewCourse(){

        return null;
    }

}
