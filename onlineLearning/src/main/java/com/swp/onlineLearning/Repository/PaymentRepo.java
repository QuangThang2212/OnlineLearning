package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.DTO.PaymentDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,String> {
    List<Payment> findByAccount(Account account);
}
