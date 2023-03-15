package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.Service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/get")
    public ResponseEntity<HashMap<String, Object>> createBlog(@RequestParam("id")String id, @RequestParam("type")String type) {
        HashMap<String, Object> json = commentService.findAllComment(id, type);

        String typeMessage = json.get("type").toString();
        if(typeMessage.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
