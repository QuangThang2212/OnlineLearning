package com.swp.onlineLearning.Model;

import javax.persistence.*;
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
public class LessonPackage implements Serializable {
    @Id
    private String PackageID;
    @Column(nullable = false)
    @Length(min = 10, max = 70)
    private String Name;
    @Column(nullable = false)
    private boolean Status;
    @Range(min = 1)
    @Column(nullable = false)
    private int PackageLocation;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "CourseID", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "lessonPackage",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;
}
