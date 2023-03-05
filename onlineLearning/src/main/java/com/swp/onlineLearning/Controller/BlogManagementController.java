package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Service.BlogService;
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
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blog")
public class BlogManagementController {
    @Autowired
    private BlogService blogService;

    @PostMapping("/create")
    public ResponseEntity<HashMap<String, Object>> createBlogType(@Valid @RequestBody BlogDTO blogDTO, BindingResult result, Principal principal) throws Exception{
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
        blogDTO.setGmail(principal.getName());
        json = blogService.save(blogDTO);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("/my_blog")
    public ResponseEntity<HashMap<String, Object>> getAllCourseType() throws Exception {
        HashMap<String, Object> json = blogService.findAll();
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<HashMap<String, Object>> updateMyBlog(@Valid @RequestBody BlogDTO blogDTO, @RequestParam("id") String id, BindingResult result) throws Exception {
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
        blogDTO.setBlogID(id);
        json = blogService.update(blogDTO);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }

}
