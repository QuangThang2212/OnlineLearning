package com.swp.onlineLearning.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTypeDTO {
    private int courseTypeID;

    @Length(min=5, max=20, message = "course type name must around 5 to 20")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Type name isn't in the right format, only allow character and number")
    private String courseTypeName;
}
