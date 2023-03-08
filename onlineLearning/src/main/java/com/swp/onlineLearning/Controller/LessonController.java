package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.QuizSubmitDTO;
import com.swp.onlineLearning.Service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/lesson")
public class LessonController {
    @Autowired
    private LessonService lessonService;
    @PostMapping("/quiz/submit")
    public ResponseEntity<HashMap<String, Object>> submitQuiz(@RequestBody QuizSubmitDTO submitDTO, Principal principal) {
        HashMap<String, Object> json = new HashMap<>();
        if(principal == null){
            json.put("msg", "Not allow access");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = lessonService.calSubmitQuiz(submitDTO, principal.getName());

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
