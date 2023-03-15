package com.swp.onlineLearning.DTO;

import com.swp.onlineLearning.Model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseFilterObjectDTO {
    private int total;
    private Page<Course> courses;
}
