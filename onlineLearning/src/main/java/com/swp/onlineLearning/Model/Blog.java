package com.swp.onlineLearning.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog implements Serializable {
    private String BlogID;
    @Column(nullable = false, unique = true, length = 70)
    @Length(min = 10, max = 70)
    private String BlogName;
    @Column(nullable = false)
    @Length(min = 10, max = 150)
    private String BlogMeta;
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private LocalDateTime CreateDate;

    private boolean ReportStatus;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "CourseTypeID", nullable = false)
    private CourseType courseType;

    @OneToMany(mappedBy = "blog",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FavouriteBlog> favouriteBlogs;
}
