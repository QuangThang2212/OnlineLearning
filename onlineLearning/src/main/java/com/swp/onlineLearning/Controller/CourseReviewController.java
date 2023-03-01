package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.EnrollInformationDTO;
import com.swp.onlineLearning.Service.CourseService;
import com.swp.onlineLearning.Service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseReviewController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;
    @PostMapping("/enroll")
    public ResponseEntity<HashMap<String, Object>> enrollCourse(@RequestBody EnrollInformationDTO enrollInformationDTO, Principal principal){
        HashMap<String, Object> json = new HashMap<>();
        if(principal == null){
            json.put("msg", "Invalid account information");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = courseService.enrollCourse(principal.getName(), enrollInformationDTO);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/lesson")
    public ResponseEntity<HashMap<String, Object>> getLessonForUser(@RequestParam("courseid") Integer courseID, @RequestParam("lessonid") Integer lessonID, Principal principal){
        HashMap<String, Object> json = new HashMap<>();
        if(principal == null){
            json.put("msg", "Invalid account information");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = lessonService.getLessonForLearning(courseID,lessonID,principal.getName());

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
