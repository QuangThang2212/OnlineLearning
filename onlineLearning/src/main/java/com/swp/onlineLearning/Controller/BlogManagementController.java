package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.Service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blog")
public class BlogManagementController {
    @Autowired
    private BlogService blogService;



    @PostMapping("/create")
    public ResponseEntity<HashMap> createBlogType(@Valid @RequestBody BlogDTO blogDTO) throws Exception{
        HashMap<String,Object> json = blogService.save(blogDTO);
        String type = "false";
        try {
            type = json.get("type").toString();
        }catch (Exception e){
            log.error("type value of save blog type message unavailable \n" +e.getMessage());
        }

        if (type.equals("true")){
            return  new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json,HttpStatus.BAD_REQUEST);
        }
    }
}
