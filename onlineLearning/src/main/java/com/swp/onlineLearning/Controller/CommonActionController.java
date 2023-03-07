package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.Service.BlogService;
import com.swp.onlineLearning.Service.CourseRateService;
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
@RequestMapping("/api/common")
public class CommonActionController {
    @Value("${role.guest}")
    private String roleGuest;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseRateService courseRateService;
    @GetMapping("/home")
    public ResponseEntity<HashMap<String, Object>> homePage(Principal principal){
        String authority;
        if(principal == null){
            authority = roleGuest;
        }else{
            authority = principal.getName();
        }
        HashMap<String, Object> json = courseService.getHomepageInfor(authority);

        return new ResponseEntity<>(json, HttpStatus.OK);
    }
    @GetMapping("/blog")
    public ResponseEntity<HashMap<String, Object>> getALlBlog(@RequestParam("page")int page, @RequestParam("limit")int limit){
        HashMap<String, Object> json = blogService.findAllBlog(page, limit);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/course/getAllCourse")
    public ResponseEntity<HashMap<String, Object>> getAllCourse(@RequestParam("limit") int limit, @RequestParam("page") int page, Principal principal){
        String authority;
        if(principal== null){
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
    @GetMapping("/course")
    public ResponseEntity<HashMap<String, Object>> CourseDetail(@RequestParam("id") Integer id, Principal principal){
        String authority;
        if(principal == null){
            authority = roleGuest;
        }else{
            authority = principal.getName();
        }
        HashMap<String, Object> json = courseService.findCourseById(authority, id);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/blog/blog_details")
    public ResponseEntity<HashMap> getBlogDetail(@RequestParam("id") String id){
        HashMap<String, Object> json = blogService.getBlogDetail(id);
        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/blog/blog_search")
    public ResponseEntity<HashMap<String, Object>> searchByNameBlog(@RequestParam("page")int page, @RequestParam("limit")int limit,@RequestParam("search") String search){
        HashMap<String, Object> json = blogService.searchByNameBlog(page, limit, search);
        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/course/rating")
    public ResponseEntity<HashMap<String, Object>> getCourseRate(@RequestParam("id")int courseID, @RequestParam("page")int page, @RequestParam("limit")int limit){
        HashMap<String, Object> json = courseRateService.getCourseRate(courseID,page, limit);
        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
