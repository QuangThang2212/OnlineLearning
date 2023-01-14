package com.swp.onlineLearning.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer implements Serializable {
    @Id
    private String AnswerID;
    @Range(min = 30)
    private String AnswerContent;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "QuestionID", nullable = false)
    private Question question;
}
