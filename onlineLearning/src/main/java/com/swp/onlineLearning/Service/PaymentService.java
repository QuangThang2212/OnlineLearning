package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Payment;

import java.util.HashMap;

public interface PaymentService {
    HashMap<String,Object> getPayment(UserDTO userDTO);
}
