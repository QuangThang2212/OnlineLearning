package com.swp.onlineLearning.DTO;

import com.swp.onlineLearning.Model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String paymentID;
    private LocalDateTime paymentAt;
    private Double amount;
    private Course course;

}
