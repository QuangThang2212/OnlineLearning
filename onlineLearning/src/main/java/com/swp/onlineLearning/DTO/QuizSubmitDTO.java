package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitDTO {
    private Integer lessonID;
    private LocalDateTime enrollTime;
    private LocalDateTime finishTime;
    private List<QuestionDTO> quiz;
}
