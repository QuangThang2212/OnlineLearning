package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRateDTO {
    private String courseRateID;
    @Range(min = 0, max = 5)
    private int stars;
    private String content;
    private int courseID;
    private int userID;
    private String userName;
    private String userImage;
}
