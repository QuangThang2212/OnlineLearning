package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRateDTO {
    private String courseRateID;
    @Range(min = 0, max = 5)
    private int starRate;
    private String content;
    private int courseID;
    private int userID;
    private String userName;
    private String userImage;
}
