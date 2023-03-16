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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int packageID;
    @Column(nullable = false)
    private String name;
    @Range(min = 1)
    @Column(nullable = false)
    private int packageLocation;

    @ManyToOne
    @JoinColumn(name = "CourseID", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "lessonPackage",cascade = CascadeType.ALL)
    private List<Lesson> lessons;
}
