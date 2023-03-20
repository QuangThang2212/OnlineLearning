package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.DTO.BlogReactDTO;
import com.swp.onlineLearning.Service.BlogReactService;
import com.swp.onlineLearning.Service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blog")
public class BlogManagementController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogReactService blogReactService;
    @Value("${role.courseExpert}")
    private String roleCourseExpert;
    @Value("${role.user}")
    private String roleUser;
    @PostMapping("/create")
    public ResponseEntity<HashMap<String, Object>> createBlogType(@Valid @RequestBody BlogDTO blogDTO, BindingResult result, Principal principal) throws Exception{
        HashMap<String, Object> json = new HashMap<>();
        ArrayList<String> strings = new ArrayList<>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                strings.add(error.getDefaultMessage());
            }
            json.put("msgProgress",strings);
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        blogDTO.setGmail(principal.getName());
        json = blogService.save(blogDTO);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }




    @PostMapping("/save")
    public ResponseEntity<HashMap<String, Object>> saveBlog(@Valid @RequestBody BlogReactDTO blogReactDTO, BindingResult result, Principal principal) throws Exception{
        HashMap<String, Object> json = new HashMap<>();
        ArrayList<String> strings = new ArrayList<>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                strings.add(error.getDefaultMessage());
            }
            json.put("msgProgress",strings);
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        blogReactDTO.setGmail(principal.getName());
        json = blogReactService.save(blogReactDTO);


        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/mark_blog")
    public ResponseEntity<HashMap<String, Object>> getMarkBlog() throws Exception {
        HashMap<String, Object> json = blogReactService.getBlogMark();
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping("/my_blog")
    public ResponseEntity<HashMap<String, Object>> getOwnerBlog(Principal principal) {
        HashMap<String, Object> json = new HashMap<>();
        if (principal == null) {
            json.put("msg", "please login to access");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = blogService.getOwnerBlog(principal.getName());
        String type = json.get("type").toString();
        if (type.equals("true")) {
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }



}
