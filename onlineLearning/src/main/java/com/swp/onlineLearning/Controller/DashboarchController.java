package com.swp.onlineLearning.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboarchController {
    @GetMapping()
    public ResponseEntity<HashMap<String, Object>> getAllCourseType() {
//        HashMap<String, Object> json = courseTypeService.findAll();
//        return new ResponseEntity<>(json, HttpStatus.OK);
        return null;
    }
}
