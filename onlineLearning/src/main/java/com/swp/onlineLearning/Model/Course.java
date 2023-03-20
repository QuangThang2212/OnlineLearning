package com.swp.onlineLearning.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseID;
    @Column(nullable = false, unique = true)
    private String courseName;
    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String description;
    @NotNull(message = "Please add image for this course")
    private String image;
    @Range(min = 0, max = 5)
    private float starRated;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime createDate;

    @Range(min = 0)
    private double price;
    @Range(min = 0)
    private int numberOfQuiz;
    @Range(min = 0)
    private int numberOfEnroll;
    @Column(nullable = false)
    @Value("false")
    @NotNull(message = "Invalid Course status value")
    private boolean status;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<LessonPackage> lessonPackages;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "expertID", nullable = false)
    private Account expertID;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CourseRate> courseRates;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "courseTypeID", nullable = false)
    private CourseType courseType;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> payments;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Voucher> vouchers;
}
