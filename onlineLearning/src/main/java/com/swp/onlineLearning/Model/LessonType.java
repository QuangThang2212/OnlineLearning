package com.swp.onlineLearning.Model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lessonTypeID;
    @Column(nullable = false, length = 40)
    @Length(max = 40)
    private String name;

    @OneToMany(mappedBy = "lessonType",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;
}
