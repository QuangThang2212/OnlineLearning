package com.swp.onlineLearning.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Account implements Serializable {
    @Id
    private String AccountID;
    @Column(nullable = false)
    private String AccountName;
    private String Password;
    private String Gmail;
    private String Avatar;
    @Column(nullable = false)
    private Boolean BanStatus;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime CreateAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "RoleID", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    @OneToMany(mappedBy = "ExpertID",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> ExpertCourses;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseRate> courseRates;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuizResult> quizResult;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Blog> blogs;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BlogReact> blogReacts;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
}
