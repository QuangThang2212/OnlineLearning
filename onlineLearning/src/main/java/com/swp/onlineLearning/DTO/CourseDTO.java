package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private int courseID;
    @Length(min = 10, max = 70, message = "Name length must in range from 10 to 70")
    private String courseName;
    @Length(min = 40, max = 240, message = "Description length must in range from 40 to 240")
    private String description;
    @NotNull(message = "Benefit length must in range from 40 to 240")
    private String benefit;
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
    @NotNull(message = "Not allow type of course null")
    private int courseTypeId;
    private String accountId;
}
