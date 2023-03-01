package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Integer questionID;
    @Length(min = 5, max=256)
    private String title;
    private int correctAnswer;
    private String answer;
    private List<String> answers;
}
