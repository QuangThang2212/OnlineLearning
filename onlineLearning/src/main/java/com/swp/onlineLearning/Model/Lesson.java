package com.swp.onlineLearning.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson implements Serializable {
    @Id
    private String PackageID;
    @Column(nullable = false, length = 70)
    @Length(min = 10, max = 70)
    private String Name;
    @Column(nullable = false, length = 250)
    @Length(min = 40, max = 240, message = "Description length must in range from 40 to 240")
    private String Description;
    @Column(nullable = false)
    private boolean Status;
    @Range(min = 1)
    @Column(nullable = false)
    private int LessonLocation;
    private String Link;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PackageID", nullable = false)
    private LessonPackage lessonPackage;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "LessonTypeID", nullable = false)
    private LessonType lessonType;

    @OneToOne(mappedBy = "lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Quiz quiz;
}
