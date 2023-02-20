package com.swp.onlineLearning.Model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private int courseTypeID;
    @NotNull
    @NotBlank
    @Column(nullable = false,unique = true)
    private String courseTypeName;

    @OneToMany(mappedBy = "courseType",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Blog> blogs;

    @OneToMany(mappedBy = "courseType",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;
    @OneToMany(mappedBy = "courseType",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Voucher> vouchers;
}
