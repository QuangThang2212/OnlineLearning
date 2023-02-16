package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Model.Account;

import java.util.HashMap;
import java.util.List;

public interface AccountService {
    HashMap<String, Object> findAll(int pageNumber);
    Account findByGmail(String gmail);
    HashMap<String, Object> save(UserDTO userDTO);
    HashMap<String, Object> activeAccount(UserDTO userDTO);
    HashMap<String, Object> update(int id);
}
