package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class UserAccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Map> login(@RequestBody UserDTO userDTO){
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        userDTO.getGmail(),
//                        userDTO.getPassword()
//                )
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, Object> json = new HashMap<>();

        json.put("msg","Login successfully");
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<HashMap> register(@RequestBody UserDTO userDTO) throws Exception {
        HashMap<String, Object> json = accountService.save(userDTO);
        String type = "false";
        try{
            type = json.get("type").toString();
        }catch (Exception e){
            log.error("type value of save account message unavailable \n" +e.getMessage());
        }

        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
