package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.RoleDTO;
import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Model.Account;

import java.util.HashMap;

public interface AccountService {
    HashMap<String, Object> findAllExcept(String gmail, int pageNumber, int size);
    Account findByGmail(String gmail);
    HashMap<String, Object> save(UserDTO userDTO);
    HashMap<String, Object> activeAccount(UserDTO userDTO);
    HashMap<String, Object> changRole(RoleDTO roleDTO);
    HashMap<String, Object> findBAllCourseExpert(String search);
    HashMap<String,Object> findUser(Integer id);
    HashMap<String,Object> findUserWithGmail(String gmail);
    HashMap<String,Object> update(UserDTO userDTO, String gmail);

}
