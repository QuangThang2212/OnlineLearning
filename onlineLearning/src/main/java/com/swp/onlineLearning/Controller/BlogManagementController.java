package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.DTO.BlogReactDTO;
import com.swp.onlineLearning.DTO.ErrorMessageDTO;
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
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blog")
public class BlogManagementController {
    @Autowired
    private BlogService blogService;

    @PostMapping("/create")
    public ResponseEntity<HashMap<String, Object>> createBlog(@Valid @RequestBody BlogDTO blogDTO, BindingResult result, Principal principal) {
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
            json.put("msgProgress", errorMessageDTOS);
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        blogDTO.setGmail(principal.getName());
        json = blogService.save(blogDTO);

        String type = json.get("type").toString();
        if (type.equals("true")) {
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/id={id}")
    public ResponseEntity<HashMap<String, Object>> updateBlog(@Valid @RequestBody BlogDTO blogDTO, BindingResult result, Principal principal, @PathVariable("id") String id) {
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
            json.put("msgProgress", errorMessageDTOS);
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        if (principal == null) {
            json.put("msg", "please login to update blog");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        blogDTO.setBlogID(id);
        json = blogService.update(blogDTO);

        String type = json.get("type").toString();
        if (type.equals("true")) {
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<HashMap<String, Object>> deleteBlog(@RequestParam("id") String id, Principal principal) {
        HashMap<String, Object> json = new HashMap<>();
        if (principal == null) {
            json.put("msg", "please login to update blog");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json= blogService.delete(id, principal.getName());

        String type = json.get("type").toString();
        if (type.equals("true")) {
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/mark_blog")
    public ResponseEntity<HashMap<String, Object>> getMarkBlog(@RequestBody BlogReactDTO blogReactDTO) {
        HashMap<String, Object> json = blogService.mark_blog(blogReactDTO);
        String type = json.get("type").toString();
        if (type.equals("true")) {
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
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
    @GetMapping("/get_favorite")
    public ResponseEntity<HashMap<String, Object>> getFavoriteBlog(Principal principal) {
        HashMap<String, Object> json = new HashMap<>();
        if (principal == null) {
            json.put("msg", "please login to access");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = blogService.getFavouriteBlog(principal.getName());
        String type = json.get("type").toString();
        if (type.equals("true")) {
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
