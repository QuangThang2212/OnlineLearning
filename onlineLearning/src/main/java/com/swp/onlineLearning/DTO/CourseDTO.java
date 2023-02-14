package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private int courseID;
    @Length(min = 10, max = 70)
    private String courseName;
    @Length(min = 40, max = 240, message = "Description length must in range from 40 to 240")
    private String description;
    private String image;
    private LocalDateTime createDate;
    @Range(min = 0)
    private double price;
    @Range(min = 0)
    private int numberOfQuiz;
    @Range(min = 1,max = 3, message = "Invalid Course status value")
    private byte status;
}
