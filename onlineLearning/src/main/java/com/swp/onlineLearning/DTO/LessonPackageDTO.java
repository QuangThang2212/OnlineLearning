package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonPackageDTO {
    private String packageID;
    @Length(min = 5, max=256)
    private String lessonTitle;
    private List<LessonDTO> numLesson;
}
