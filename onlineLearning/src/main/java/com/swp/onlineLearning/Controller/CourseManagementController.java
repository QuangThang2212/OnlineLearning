package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.CourseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseManagementController {
    @PostMapping("/create")
    public ResponseEntity<HashMap> createNewCourse(@RequestBody CourseDTO CourseDTO){

        return null;
    }
}
