package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.CourseRateDTO;
import com.swp.onlineLearning.DTO.EnrollInformationDTO;
import com.swp.onlineLearning.Service.CommentService;
import com.swp.onlineLearning.Service.CourseRateService;
import com.swp.onlineLearning.Service.CourseService;
import com.swp.onlineLearning.Service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @Autowired
    private CourseRateService courseRateService;
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
    @PostMapping("/rating/create")
    public ResponseEntity<HashMap<String, Object>> sendRatingOfUser(@Valid @RequestBody CourseRateDTO courseRateDTO, BindingResult result, Principal principal){
        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, Object> json = new HashMap<>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                stringBuilder.append(error.getDefaultMessage()).append("\n");
            }
            json.put("msg",stringBuilder.toString());
            return new ResponseEntity<>(json,HttpStatus.BAD_REQUEST);
        }
        if(principal == null){
            json.put("msg", "Invalid account information");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = courseRateService.createCourseRate(courseRateDTO,principal.getName());

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get_purchase_course")
    public ResponseEntity<HashMap<String, Object>> getPurchaseCourse(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("search") String search){
        HashMap<String, Object> json = courseService.findAllPurchaseCourse(page,limit,search);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }

}
