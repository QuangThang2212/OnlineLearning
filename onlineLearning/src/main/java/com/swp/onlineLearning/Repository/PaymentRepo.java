package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,String> {
}
