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
public class CourseRate implements Serializable {
    @Id
    private String courseRateID;
    @NotNull
    @Column(nullable = false)
    private boolean enrollStatus;
    @Range(min = 1)
    private int lessonLocation;
    @NotNull
    @NotBlank
    private LocalDateTime enrollTime;
    private String content;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "CourseID", nullable = false)
    private Course course;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;
}
