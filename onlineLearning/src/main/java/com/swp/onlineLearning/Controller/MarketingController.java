package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.MarketingDTO;
import com.swp.onlineLearning.Service.MarketingService;
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
@RequestMapping("/api/marketing")
public class MarketingController {
    @Autowired
    private MarketingService marketingService;

    @PostMapping("/create")
    public ResponseEntity<HashMap<String, Object>> createMarketing(@Valid @RequestBody MarketingDTO marketingDTO, BindingResult result) {
        HashMap<String, Object> json = new HashMap<>();
        StringBuilder strings = new StringBuilder();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                strings.append(error.getDefaultMessage());
            }
            json.put("msg",strings.toString());
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = marketingService.save(marketingDTO);

        String type = json.get("type").toString();
        if (type.equals("true")) {
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);

        }
    }
}


