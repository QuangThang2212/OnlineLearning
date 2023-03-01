package com.swp.onlineLearning.Model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lessonID;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String description;
    @Range(min = 1)
    @Column(nullable = false)
    private int lessonLocation;
    private String link;
    private double time;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PackageID", nullable = false)
    @JsonIgnore
    private LessonPackage lessonPackage;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LessonTypeID", nullable = false)
    @JsonIgnore
    private LessonType lessonType;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Question> questions;
    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;
    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<QuizResult> quizResults;
    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CourseRate> courseRates;
}
