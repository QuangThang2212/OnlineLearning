package com.swp.onlineLearning.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    @Range(min = 0)
    private int commentLocation;
    private byte reportStatus;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentID")
    private Comment parentID;
    @OneToOne(mappedBy = "parentID",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Comment parent;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "BlogID")
    private Blog blog;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "LessonID")
    private Lesson lesson;
}
