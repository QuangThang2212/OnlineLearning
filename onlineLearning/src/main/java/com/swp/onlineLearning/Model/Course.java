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
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseID;
    @Column(nullable = false, unique = true, length = 70)
    @Length(min = 10, max = 70)
    private String courseName;
    @Column(nullable = false, length = 250)
    @Length(min = 40, max = 240, message = "Description length must in range from 40 to 240")
    private String description;
    @Column(nullable = false)
    @NotNull(message = "Benefit length must in range from 40 to 240")
    private String benefit;
    private String image;
    @Range(min = 1, max = 5)
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
    @Range(min = 0)
    private int numberOfFavorite;
    @Column(nullable = false)
    @NotNull(message = "Invalid Course status value")
    private boolean status;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LessonPackage> lessonPackages;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "expertID")
    private Account expertID;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseRate> courseRates;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "courseTypeID", nullable = false)
    private CourseType courseType;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Voucher> vouchers;
}
