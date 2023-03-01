package com.swp.onlineLearning.DTO;

import com.swp.onlineLearning.Model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Integer courseID;
    @Length(min = 5, max = 200, message = "Name length must in range from 5 to 200")
    private String courseName;
    @Length(min = 200, message = "Description length must greater than 200")
    private String description;
    @NotNull(message = "Please add image for this course")
    private String image;
    private LocalDateTime createDate;
    @Range(min = 0)
    @Value("0")
    private double price;
    @Range(min = 0)
    @Value("0")
    private int numberOfQuiz;
    @Value("false")
    private boolean status;
    private int accountID;
    private Account courseExpert;
    @Value("0")
    private float starRated;
    @Value("0")
    private int numberOfEnroll;

    private String courseExpertName;

    @NotNull(message = "Not allow type of course null")
    private int courseTypeID;
    private String typeName;
    private List<LessonPackageDTO> lessonPackages;


}
