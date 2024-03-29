package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.CommentDTO;
import com.swp.onlineLearning.Service.CommentReportService;
import com.swp.onlineLearning.Service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentReportService commentReportService;

    @GetMapping("/get")
    public ResponseEntity<HashMap<String, Object>> getAllComment(@RequestParam("id")String id, @RequestParam("type")String type, @RequestParam("page") int page) {
        HashMap<String, Object> json = commentService.findAllComment(id, type, page);

        String typeMessage = json.get("type").toString();
        if(typeMessage.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<HashMap<String, Object>> createComment(@RequestBody CommentDTO commentDTO, Principal principal, BindingResult result) {
        HashMap<String, Object> json = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                stringBuilder.append(error.getDefaultMessage()).append(" ");
            }
            json.put("msg",stringBuilder);
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        if(principal==null){
            json.put("msg","please login to comment");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        try{
            json = commentService.createComment(commentDTO, principal.getName());
        }catch (Exception e){
            log.error("Create comment fail \n"+e.getMessage());
            json.put("msg","Create comment fail, please try again");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }

        String typeMessage = json.get("type").toString();
        if(typeMessage.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/update")
    public ResponseEntity<HashMap<String, Object>> updateComment(@RequestBody CommentDTO commentDTO, Principal principal, BindingResult result) {
        HashMap<String, Object> json = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                stringBuilder.append(error.getDefaultMessage()).append(" ");
            }
            json.put("msg",stringBuilder);
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        if(principal==null){
            json.put("msg","please login to comment");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        try{
            json = commentService.updateComment(commentDTO, principal.getName());
        }catch (Exception e){
            log.error("Update fail \n"+e.getMessage());
            json.put("msg","Update fail");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }

        String typeMessage = json.get("type").toString();
        if(typeMessage.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/delete/{id}")
    public ResponseEntity<HashMap<String, Object>> deleteComment(@PathVariable("id") String id, Principal principal) {
        HashMap<String, Object> json = new HashMap<>();
        if(principal==null){
            json.put("msg","please login to comment");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        try{
            json = commentService.deleteComment(id, principal.getName());
        }catch (Exception e){
            log.error("Delete fail \n"+e.getMessage());
            json.put("msg","Delete fail");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }

        String typeMessage = json.get("type").toString();
        if(typeMessage.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/report")
    public ResponseEntity<HashMap<String, Object>> reportComment(@RequestParam("id")String id, @RequestParam("type")String type, Principal principal) {
        HashMap<String, Object> json = new HashMap<>();
        if(principal==null){
            json.put("msg","please login to comment");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = commentReportService.reportCommentAndBlog(id,type, principal.getName());

        String typeMessage = json.get("type").toString();
        if(typeMessage.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
