package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.Service.CourseService;
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
    @Value("${role.guest}")
    private String roleGuest;

    @GetMapping()
    public ResponseEntity<HashMap<String, Object>> CourseDetail(@RequestParam("id") Integer id){

        return null;
    }
    @GetMapping("/get")
    public ResponseEntity<HashMap<String, Object>> findAllCourse(@RequestParam("page") int page, @RequestParam("limit") int limit, Principal principal){
        String authority;
        if(principal == null){
            authority = roleGuest;
        }else{
            authority = principal.getName();
        }
        HashMap<String, Object> json = courseService.findAll(page, limit, authority);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
