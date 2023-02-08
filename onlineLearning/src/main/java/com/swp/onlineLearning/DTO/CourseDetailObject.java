package com.swp.onlineLearning.DTO;

import com.swp.onlineLearning.Model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int CourseID;
    @Column(nullable = false, unique = true, length = 70)
    @Length(min = 10, max = 70)
    private String CourseName;
    @Column(nullable = false, length = 250)
    @Length(min = 40, max = 240, message = "Description length must in range from 40 to 240")
    private String Description;
    @Range(min = 1, max = 5)
    private float StarRated;
    @NotNull
    private LocalDateTime CreateDate;
    @Range(min = 0)
    private double Price;
    @Range(min = 0)
    private int NumberOfQuiz;
    @Range(min = 0)
    private int NumberOfEnroll;
    private byte Status;
    private List<LessonPackage> lessonPackages;
    private String ExpertID;
    private String ExpertName;
    private List<CourseRate> courseRates;
    private CourseType courseType;
}
