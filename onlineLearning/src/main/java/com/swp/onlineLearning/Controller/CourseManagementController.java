package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.CourseDTO;
import com.swp.onlineLearning.Service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseManagementController {
    @Autowired
    private CourseService courseService;
    @PostMapping("/create")
    public ResponseEntity<HashMap> createNewCourse(@Valid @RequestBody CourseDTO courseDTO, BindingResult result){
        HashMap<String, Object> json = new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                stringBuffer.append(error.getDefaultMessage()) ;
                stringBuffer.append("\n") ;
            }
            json.put("msg",stringBuffer.toString());
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = courseService.save(courseDTO);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
