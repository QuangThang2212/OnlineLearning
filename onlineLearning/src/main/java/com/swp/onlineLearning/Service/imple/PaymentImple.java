package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.PaymentDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Payment;
import com.swp.onlineLearning.Repository.AccountRepo;
import com.swp.onlineLearning.Repository.PaymentRepo;
import com.swp.onlineLearning.Service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@Transactional
public class PaymentImple implements PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private AccountRepo accountRepo;

    @Override
    public HashMap<String, Object> getPayment(String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Account account = accountRepo.findByGmail(gmail);
        List<Payment> payments = paymentRepo.findByAccount(account);
        if (payments.isEmpty()) {
            log.info("0 payments history founded");
            json.put("type", true);
            return json;
        }

        List<PaymentDTO> paymentDTOS = new ArrayList<>();
        for (Payment a : payments) {
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setPaymentAt(a.getPaymentAt());
            paymentDTO.setPaymentID(a.getPaymentID());
            paymentDTO.setCourse(a.getCourse());

            paymentDTOS.add(paymentDTO);
        }
        log.info("successfully");
        json.put("payments", paymentDTOS);
        json.put("msg", "successfully");
        json.put("type", true);
        return json;
    }
}

