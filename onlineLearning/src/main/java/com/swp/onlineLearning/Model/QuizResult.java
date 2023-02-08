package com.swp.onlineLearning.Model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private String quizResult;
    @Range(min = 0, max = 10)
    @Column(nullable = false)
    private float result;
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private LocalDateTime enrollTimel;
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private LocalDateTime finishTime;
    private boolean status;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "QuizID", nullable = false)
    private Quiz quiz;
}