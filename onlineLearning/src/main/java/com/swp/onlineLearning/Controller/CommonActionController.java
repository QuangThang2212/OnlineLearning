package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.Service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class CommonActionController {
    @Autowired
    private CourseService courseService;
    @GetMapping()
    public ResponseEntity<HashMap> homePage(){
        HashMap<String, Object> json = courseService.getHomepageInfor();

        return new ResponseEntity<>(json, HttpStatus.OK);
    }
    @GetMapping("/course?id={id}")
    public ResponseEntity<HashMap> CourseDetail(@PathVariable String id){

        return null;
    }
}
