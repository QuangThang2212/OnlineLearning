package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.CourseTypeDTO;
import com.swp.onlineLearning.Service.CourseTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/type_course")

public class CourseTypeController {
    @Autowired
    private CourseTypeService courseTypeService;
    @GetMapping()
    public ResponseEntity<HashMap<String, Object>> getAllCourseType() throws Exception {
        HashMap<String, Object> json = courseTypeService.findAll();
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<HashMap<String, Object>> createCourseType(@Valid @RequestBody CourseTypeDTO courseTypeDTO, BindingResult result) {
        HashMap<String, Object> json = new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                stringBuffer.append(error.getDefaultMessage()) ;
                stringBuffer.append("\n") ;
            }
            json.put("msg",stringBuffer.toString());
            return new ResponseEntity<>(json,HttpStatus.BAD_REQUEST);
        }
        json = courseTypeService.save(courseTypeDTO);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/update/id={id}")
    public ResponseEntity<HashMap<String, Object>> updateCourseType(@Valid @RequestBody CourseTypeDTO courseTypeDTO, BindingResult result, @PathVariable("id") int id) throws Exception {
        HashMap<String, Object> json = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                stringBuilder.append(error.getDefaultMessage()) ;
                stringBuilder.append("\n") ;
            }
            json.put("msg",stringBuilder.toString());
            return new ResponseEntity<>(json,HttpStatus.BAD_REQUEST);
        }
        courseTypeDTO.setCourseTypeID(id);
        json = courseTypeService.update(courseTypeDTO);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/delete/id={id}")
    public ResponseEntity<HashMap<String, Object>> deleteCourseType(@PathVariable("id") int id){
        HashMap<String, Object> json = courseTypeService.delete(id);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
