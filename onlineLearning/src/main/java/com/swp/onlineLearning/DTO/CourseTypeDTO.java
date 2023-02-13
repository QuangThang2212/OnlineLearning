package com.swp.onlineLearning.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTypeDTO {
    private int courseTypeID;
    @NotNull
    @NotBlank
    private String courseTypeName;
}
