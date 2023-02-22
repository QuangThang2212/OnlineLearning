package com.swp.onlineLearning.Model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson implements Serializable {
    @Id
    private String lessonID;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String description;
    @Range(min = 1)
    @Column(nullable = false)
    private int lessonLocation;
    private String link;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PackageID", nullable = false)
    @JsonIgnore
    private LessonPackage lessonPackage;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "LessonTypeID", nullable = false)
    @JsonIgnore
    private LessonType lessonType;

    @OneToOne(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Quiz quiz;
    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> comments;
}
