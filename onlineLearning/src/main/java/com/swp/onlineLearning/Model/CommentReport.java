package com.swp.onlineLearning.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReport implements Serializable {

    @GeneratedValue()
    private int id;
    private Account account;
    private Comment comment;
    private LocalDate reportTime;
}
