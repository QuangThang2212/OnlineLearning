package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.CourseTypeDTO;
import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Service.CourseTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<HashMap> createCourseType(@RequestBody CourseTypeDTO courseTypeDTO) throws Exception {
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

}
