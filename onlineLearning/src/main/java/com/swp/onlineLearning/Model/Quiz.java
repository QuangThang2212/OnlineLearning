package com.swp.onlineLearning.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz implements Serializable {
    @Id
    private String QuizID;
    @Range(min = 0, max = 10)
    private float PassCondition;
    @Range(min = 30)
    private float Duration;
    @Range(min = 0)
    private float TimeBeforeRetake;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "LessonID", nullable = false, unique = true)
    private Lesson lesson;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Question question;
}
