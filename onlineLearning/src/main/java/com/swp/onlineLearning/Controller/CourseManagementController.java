package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.CourseDTO;
import com.swp.onlineLearning.DTO.ListOfPackageDTO;
import com.swp.onlineLearning.Service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/update_pakage/id={id}")
    public ResponseEntity<HashMap> createNewCourse(@RequestBody ListOfPackageDTO lessonPackageDTOS, @PathVariable("id") int id){
        System.out.println(lessonPackageDTOS);
        System.out.println(id);
//        HashMap<String, Object> json = new HashMap<>();
//        StringBuffer stringBuffer = new StringBuffer();
//        if (result.hasErrors()) {
//            for (FieldError error : result.getFieldErrors()) {
//                stringBuffer.append(error.getDefaultMessage()) ;
//                stringBuffer.append("\n") ;
//            }
//            json.put("msg",stringBuffer.toString());
//            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
//        }
//        json = courseService.save(courseDTO);
//
//        String type = json.get("type").toString();
//        if(type.equals("true")){
//            return new ResponseEntity<>(json, HttpStatus.OK);
//        }else{
//            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
//        }
        return null;
    }
    @PostMapping("/getAllCourse")
    public ResponseEntity<HashMap> getAllCourse(@RequestParam("limit") int limit, @RequestParam("page") int page){
//        HashMap<String, Object> json = courseService.save(courseDTO);
//
//        String type = json.get("type").toString();
//        if(type.equals("true")){
//            return new ResponseEntity<>(json, HttpStatus.OK);
//        }else{
//            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
//        }
        return null;
    }
}
