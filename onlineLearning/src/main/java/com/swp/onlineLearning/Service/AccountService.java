package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.RoleDTO;
import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Model.Account;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;

public interface AccountService {
    HashMap<String, Object> findAllExcept(String gmail, int pageNumber, int size);
    HashMap<String, Object> save(UserDTO userDTO);
    HashMap<String, Object> activeAccount(UserDTO userDTO);

    HashMap<String, Object> update(UserDTO userDTO, String gmail);

    HashMap<String, Object> changRole(RoleDTO roleDTO);
    HashMap<String, Object> findBAllCourseExpert();
    Account findByGmail(String gmail);
    HashMap<String,Object> findUser(String gmail);

}
