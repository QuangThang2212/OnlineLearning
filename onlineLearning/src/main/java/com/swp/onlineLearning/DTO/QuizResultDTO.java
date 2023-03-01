package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultDTO {
    private String quizResult;
    private float result;
    private LocalDateTime enrollTime;
    private boolean quizStatus;
}
