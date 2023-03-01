package com.swp.onlineLearning.DTO;

import com.swp.onlineLearning.Model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDTO {
    private Integer lessonID;
    @Length(min = 5, max = 250)
    private String title;
    @Length(min = 5)
    private String description;
    private String link;
    private double time;
    @NotNull
    private String type;
    private List<QuestionDTO> value;
    private List<CommentDTO> comments;
    private String lessonPackageName;
    private QuizResultDTO quizResultDTO;
}
