package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.RoleDTO;
import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Service.AccountService;
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
    public ResponseEntity<HashMap<String, Object>> changeRoleOfAccount(@PathVariable("id") Integer id, @RequestBody RoleDTO roleDTO) {
        roleDTO.setAccountID(id);
        HashMap<String, Object> json = accountService.changRole(roleDTO);

        String type = json.get("type").toString();
        if (type.equals("true")) {
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get_user")
    public ResponseEntity<HashMap<String, Object>> getUser(Principal principal) {
        HashMap<String, Object> json = new HashMap<>();
        if(principal==null){
            json.put("msg", "Invalid account");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = accountService.findUser(principal.getName());
        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/update")
    public ResponseEntity<HashMap<String, Object>> getUserInformation(@Valid @RequestBody UserDTO userDTO, BindingResult result, Principal principal) {
        HashMap<String, Object> json = new HashMap<>();
        ArrayList<String> strings = new ArrayList<>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                strings.add(error.getDefaultMessage());
            }
            json.put("msgProgress",strings);
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        if(principal==null){
            json.put("msg", "Invalid account");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = accountService.update(userDTO, principal.getName());
        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json,HttpStatus.OK);
        }else return new ResponseEntity<>(json,HttpStatus.BAD_REQUEST);

    }
}
