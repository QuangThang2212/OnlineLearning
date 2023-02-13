package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Model.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AccountService {
    List<Account> findAll();
    Account findByGmailAndPassword(Account account);
    HashMap<String, Object> save(UserDTO userDTO);
    Account update(UserDTO userDTO);
}
