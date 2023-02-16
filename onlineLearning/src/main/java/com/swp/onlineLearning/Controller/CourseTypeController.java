package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.CourseTypeDTO;
import com.swp.onlineLearning.Service.CourseTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<HashMap> getAllCourseType() throws Exception {
        HashMap<String, Object> json = courseTypeService.findAll();
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<HashMap> createCourseType(@Valid @RequestBody CourseTypeDTO courseTypeDTO) throws Exception {
        HashMap<String, Object> json = courseTypeService.save(courseTypeDTO);
        String type = "false";
        try{
            type = json.get("type").toString();
        }catch (Exception e){
            log.error("type value of save course type message unavailable \n" +e.getMessage());
        }
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/update/id={id}")
    public ResponseEntity<HashMap> updateCourseType(@Valid @RequestBody CourseTypeDTO courseTypeDTO,@PathVariable("id") int id) throws Exception {
        courseTypeDTO.setCourseTypeID(id);
        HashMap<String, Object> json = courseTypeService.update(courseTypeDTO);
        String type = "false";
        try{
            type = json.get("type").toString();
        }catch (Exception e){
            log.error("type value of save course type message unavailable \n" +e.getMessage());
        }
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/delete/id={id}")
    public ResponseEntity<HashMap> deleteCourseType(@PathVariable("id") int id) throws Exception {
        HashMap<String, Object> json = courseTypeService.delete(id);
        String type = "false";
        try{
            type = json.get("type").toString();
        }catch (Exception e){
            log.error("type value of save course type message unavailable \n" +e.getMessage());
        }
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
