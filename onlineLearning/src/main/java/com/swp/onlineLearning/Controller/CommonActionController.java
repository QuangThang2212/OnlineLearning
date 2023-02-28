package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.Service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class CommonActionController {
    @Value("${role.guest}")
    private String roleGuest;
    @Autowired
    private CourseService courseService;
    @GetMapping()
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
}
