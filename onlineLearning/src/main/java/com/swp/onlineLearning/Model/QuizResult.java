package com.swp.onlineLearning.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResult implements Serializable {
    @Id
    private String QuizResult;
    @Range(min = 0, max = 10)
    @Column(nullable = false)
    private float Result;
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private LocalDateTime EnrollTimel;
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private LocalDateTime FinishTime;
    private boolean Status;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "QuizID", nullable = false)
    private Quiz quiz;
}