package com.swp.onlineLearning.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    @Id
    private String paymentID;
    @Range(min = 0)
    @Column(nullable = false)
    private Double amount;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime paymentAt;
    @Column(length = 100)
    private String toPerson;
    @ManyToOne
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;
    @ManyToOne
    @JoinColumn(name = "CourseID", nullable = false)
    private Course course;
    @ManyToOne
    @JoinColumn(name = "VoucherID")
    private Voucher voucher;
}
