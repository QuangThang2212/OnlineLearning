package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.HomePageObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommonActionController {
    public ResponseEntity<HomePageObject> homePage(){
        return null;
    }
}
