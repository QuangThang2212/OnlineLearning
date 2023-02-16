package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.Config.JWTUtil;
import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<HashMap> login(@RequestBody UserDTO userDTO) {
        HashMap<String, Object> json = new HashMap<>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getGmail(), userDTO.getPassword()));
            json.put("msg","Login successfully");
        } catch (BadCredentialsException e) {
            log.error("Incorrect username or password \n" + e.getMessage());
            json.put("msg","Incorrect username or password");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        final Account account = accountService.findByGmail(userDTO.getGmail());
        final String token = jwtUtil.generateToken(account);
        if(token==null){
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json.put("token",token);
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
