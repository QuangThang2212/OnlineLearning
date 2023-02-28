package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.RoleDTO;
import com.swp.onlineLearning.Service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/account")
public class AccountManagerController {
    @Autowired
    private AccountService accountService;

    @GetMapping()
    public ResponseEntity<HashMap<String, Object>> getAllAccount(@RequestParam("page") int page, @RequestParam("limit") int limit, Principal principal) {

        HashMap<String, Object> json = accountService.findAllExcept(principal.getName(), page, limit);

        String type = json.get("type").toString();
        if (type.equals("true")) {
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/course_expert")
    public ResponseEntity<HashMap<String, Object>> getAllCourseExpert() {
        HashMap<String, Object> json = accountService.findBAllCourseExpert();

        String type = json.get("type").toString();
        if (type.equals("true")) {
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/change_role/id={id}")
    public ResponseEntity<HashMap<String, Object>> changeRoleOfAccount(@PathVariable("id") int id, @RequestBody RoleDTO roleDTO) {
        roleDTO.setAccountID(id);
        HashMap<String, Object> json = accountService.changRole(roleDTO);

        String type = json.get("type").toString();
        if (type.equals("true")) {
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
