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
public class CourseRate implements Serializable {
    @Id
    private String CourseRateID;
    @NotNull
    @Column(nullable = false)
    private boolean EnrollStatus;
    @Range(min = 1)
    private int LessonLocation;
    @NotNull
    @NotBlank
    private LocalDateTime EnrollTime;
    private String content;

    @NotNull
    @NotBlank
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "CourseID", nullable = false)
    private Course course;

    @NotNull
    @NotBlank
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;
}
