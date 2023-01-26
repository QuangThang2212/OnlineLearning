package com.swp.onlineLearning.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    @Id
    private String CommentID;
    private String comment;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime CreateAt;
    private LocalDateTime UpdateAt;
    private boolean ReportStatus;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentID")
    private Comment parentID;
    @OneToOne(mappedBy = "parentID",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Comment Parent;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "BlogID")
    private Blog blog;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "LessonID")
    private Lesson lesson;
}
