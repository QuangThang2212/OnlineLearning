package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.Config.JWTUtil;
import com.swp.onlineLearning.DTO.ErrorMessageDTO;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class UserAccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody UserDTO userDTO) {
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

        final String token = JWTUtil.generateToken(account);
        if(token==null){
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json.put("token",token);
        json.put("name",account.getName());
        json.put("image",account.getImage());
        json.put("id",account.getAccountID());
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<HashMap<String, Object>> register(@Valid @RequestBody UserDTO userDTO, BindingResult result){
        HashMap<String, Object> json = new HashMap<>();
        ArrayList<ErrorMessageDTO> errorMessageDTOS = new ArrayList<>();
        ErrorMessageDTO errorMessageDTO;
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errorMessageDTO = new ErrorMessageDTO();
                errorMessageDTO.setErrorName(error.getField());
                errorMessageDTO.setMessage(error.getDefaultMessage());
                errorMessageDTOS.add(errorMessageDTO);
            }
            json.put("msgProgress",errorMessageDTOS);
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        json = accountService.save(userDTO);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/active")
    public ResponseEntity<HashMap<String, Object>> active(HttpServletRequest request){
        final String token = request.getHeader("token");
        HashMap<String, Object> json = new HashMap<>();
        try{
            json = accountService.activeAccount(token);
        }catch (Exception e){
            log.error(e.getMessage());
            json.put("msg", "Save user with gmail fail");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/forgot_password")
    public ResponseEntity<HashMap<String, Object>> forgotPass(@PathVariable("gmail") String gmail){
        HashMap<String, Object> json = accountService.forgotPassword(gmail);

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/change_password")
    public ResponseEntity<HashMap<String, Object>> changePassword(@PathVariable("password") String password, HttpServletRequest request){
        final String token = request.getHeader("token");
        HashMap<String, Object> json = new HashMap<>();
        try{
            json = accountService.changePassword(password, token);
        }catch (Exception e){
            log.error(e.getMessage());
            json.put("msg", "Change password fail");
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }

        String type = json.get("type").toString();
        if(type.equals("true")){
            return new ResponseEntity<>(json, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
    }
}
