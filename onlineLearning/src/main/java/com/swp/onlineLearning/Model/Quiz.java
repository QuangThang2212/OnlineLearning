package com.swp.onlineLearning.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz implements Serializable {
    @Id
    private String quizID;
    @Range(min = 0, max = 10)
    private float passCondition;
    @Range(min = 30)
    private float duration;
    @Range(min = 0)
    private float timeBeforeRetake;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "LessonID", nullable = false, unique = true)
    private Lesson lesson;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> question;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuizResult> quizResult;
}
