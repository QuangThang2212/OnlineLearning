package com.swp.onlineLearning.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int CourseTypeID;
    @NotNull
    @NotBlank
    @Column(nullable = false,unique = true)
    private String CourseTypeName;

    @OneToMany(mappedBy = "courseType",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Blog> blogs;

    @OneToMany(mappedBy = "courseType",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;
}
