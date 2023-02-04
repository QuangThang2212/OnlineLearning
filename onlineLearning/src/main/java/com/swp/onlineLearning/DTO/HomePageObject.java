package com.swp.onlineLearning.DTO;

import com.swp.onlineLearning.Model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomePageObject {
    private List<Course> PopularCourse;
    private List<Course> NewestCourse;
    private List<Course> FreePopularCourse;
    private List<Course> FamousPaidCourses;
}
