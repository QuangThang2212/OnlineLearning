package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.CourseDTO;
import com.swp.onlineLearning.DTO.ErrorMessageDTO;
import com.swp.onlineLearning.DTO.ListOfCourseDTO;
import com.swp.onlineLearning.DTO.ListOfPackageDTO;
import com.swp.onlineLearning.Service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
@Slf4j
public class CourseManagementController {
    @Autowired
    private CourseService courseService;
    @PostMapping("/create")
    public ResponseEntity<HashMap<String, Object>> createNewCourse(@Valid @RequestBody CourseDTO courseDTO, BindingResult result){
        HashMap<String, Object> json = new HashMap<>();
        ArrayList<ErrorMessageDTO> errorMessageDTOS = new ArrayList<>();
        ErrorMessageDTO errorMessageDTO;
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errorMessageDTO = new ErrorMessageDTO();
                errorMessageDTO.setErrorName(error.getField());
                errorMessageDTO.setMessage(error.getDefaultMessage());
                errorMessageDTOS.add(errorMessageDTO);
            }
            json.put("msgProgress",errorMessageDTOS);
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
    public ResponseEntity<HashMap<String, Object>> updatePackageOfTopic(@RequestBody ListOfPackageDTO lessonPackageDTOS, @PathVariable("id") Integer id){
        HashMap<String, Object> json = new HashMap<>();
        if(id==null){
            json.put("msg", "Not allow id course null");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        try{
            json = courseService.saveLessonPackage(lessonPackageDTOS, id);
        }catch (Exception e) {
            log.error("Update topics fail\n"+e.getMessage());
            json.put("msg", "Update topics fail");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/change_status")
    public ResponseEntity<HashMap<String, Object>> changeCourseStatus(@RequestBody ListOfCourseDTO ListOfCourseDTO){
        HashMap<String, Object> json = courseService.changeCourseStatus(ListOfCourseDTO);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<HashMap<String, Object>> changeCourseStatus(@RequestParam("id") Integer id, Principal principal){
        HashMap<String, Object> json = new HashMap<>();
        if(principal==null){
            json.put("msg", "Invalid account");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = courseService.deleteCourse(principal.getName(),id);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin_get")
    public ResponseEntity<HashMap<String, Object>> getCourseUpdate(@RequestParam("id") Integer id){
        HashMap<String, Object> json = courseService.findCourseByIdToUpdate(id);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
