package com.swp.onlineLearning.DTO;

import lombok.Data;

import java.util.List;
@Data
public class ListOfPackageDTO {
    private List<LessonPackageDTO> lessonPakages;

    private List<Integer> deletePackage;
    private List<Integer> deleteLesson;
    private List<Integer> deleteQuestion;
}
