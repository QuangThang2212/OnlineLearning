package com.swp.onlineLearning.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    @Id
    private String commentID;
    private String comment;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    @ManyToOne
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;
    @ManyToOne
    @JoinColumn(name = "ParentID")
    private Comment parentID;
    @OneToMany(mappedBy = "parentID",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> childComment;

    @ManyToOne
    @JoinColumn(name = "BlogID")
    private Blog blog;
    @ManyToOne
    @JoinColumn(name = "LessonID")
    private Lesson lesson;
    @OneToMany(mappedBy = "comment",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentReport> commentReports;
}
