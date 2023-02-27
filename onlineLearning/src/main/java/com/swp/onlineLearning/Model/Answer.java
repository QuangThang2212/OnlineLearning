package com.swp.onlineLearning.Model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int answerID;
    private String answerContent;
    private boolean rightAnswer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "QuestionID", nullable = false)
    private Question question;
}
