package com.swp.onlineLearning.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportID;
    private LocalDateTime reportAt;
    @ManyToOne
    @JoinColumn(name = "CommentID")
    private Comment comment;
    @ManyToOne
    @JoinColumn(name = "AccountID")
    private Account account;
}
