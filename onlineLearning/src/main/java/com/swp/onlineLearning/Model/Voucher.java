package com.swp.onlineLearning.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voucherID;
    @Column(nullable = false,unique = true, length = 70)
    @Length(min = 5, max = 50)
    private String name;
    @Column(nullable = false, length = 250)
    @Length(min = 20, max = 100, message = "Description length must in range from 20 to 100")
    private String description;

    @Range(min = 0)
    @Column(nullable = false)
    private double amount;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime startDate;
    @NotNull
    @Column(nullable = false)
    private boolean status;
    @NotNull
    @Column(nullable = false)
    private float startApply;
    @NotNull
    @Column(nullable = false)
    private float duration;
    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> payments;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "CourseID")
    private Course course;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "CourseTypeID")
    private CourseType courseType;
}
