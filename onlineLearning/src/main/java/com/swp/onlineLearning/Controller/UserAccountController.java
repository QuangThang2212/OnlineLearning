package com.swp.onlineLearning.Controller;

import com.swp.onlineLearning.Model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserAccountController {
    @GetMapping("/login")
    public ResponseEntity<Account> login(){
        Account account = new Account();
        account.setAccountName("aquang thang");
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        System.out.println(account.getGmail());
        System.out.println(account.getPassword());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}
