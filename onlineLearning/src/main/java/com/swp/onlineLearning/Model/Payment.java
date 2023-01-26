package com.swp.onlineLearning.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Double Amount;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime PaymentAt;
    @NotNull
    @Column(nullable = false)
    private String to;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "CourseID", nullable = false)
    private Course course;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "VoucherID")
    private Voucher voucher;
}
