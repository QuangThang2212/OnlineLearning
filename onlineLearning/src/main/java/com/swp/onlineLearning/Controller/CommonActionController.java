package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.HomePageObject;
import com.swp.onlineLearning.Service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommonActionController {
    @Autowired
    private CourseService courseService;
    @GetMapping({"/","/home"})
    public ResponseEntity<HomePageObject> homePage(){
        HomePageObject homePageObject = courseService.getHomepageInfor();

        return new ResponseEntity<>(homePageObject, HttpStatus.OK);
    }
    @GetMapping("/course?id={id}")
    public ResponseEntity<HomePageObject> CourseDetail(@PathVariable String id){

        return null;
    }
}
