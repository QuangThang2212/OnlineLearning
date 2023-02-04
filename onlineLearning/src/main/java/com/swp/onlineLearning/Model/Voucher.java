package com.swp.onlineLearning.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher implements Serializable {
    @Id
    private String VoucherID;
    @Column(nullable = false, length = 70)
    @Length(min = 10, max = 70)
    private String Name;
    @Column(nullable = false, length = 250)
    @Length(min = 40, max = 240, message = "Description length must in range from 40 to 240")
    private String Description;

    @Range(min = 0)
    @Column(nullable = false)
    private double Amount;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime StartDate;
    @NotNull
    @Column(nullable = false)
    private boolean Status;
    @NotNull
    @Column(nullable = false)
    private float StartApply;
    @NotNull
    @Column(nullable = false)
    private float Duration;
    @OneToMany(mappedBy = "voucher", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Payment> payments;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "CourseID")
    private Course course;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "CourseTypeID")
    private CourseType courseType;
}
