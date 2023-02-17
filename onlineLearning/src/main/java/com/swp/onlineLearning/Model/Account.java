package com.swp.onlineLearning.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
    @Id
    private String accountID;
    @Column(nullable = false)
    private String name;
    private String password;
    @Column(unique = true)
    private String gmail;
    private String image;
    @Column(nullable = false)
    private Boolean banStatus;
    @Column(nullable = false)
    private LocalDateTime createAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RoleID", nullable = false)
    private RoleUser roleUser;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> payments;

    @OneToMany(mappedBy = "expertID",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> expertCourses;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CourseRate> courseRates;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<QuizResult> quizResult;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Blog> blogs;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BlogReact> blogReacts;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

}
