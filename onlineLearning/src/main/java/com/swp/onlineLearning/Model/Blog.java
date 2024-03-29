package com.swp.onlineLearning.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog implements Serializable {

    @Id
    private String blogID;
    @Column(nullable = false, unique = true)
    private String blogName;
    @Column(nullable = false, length = 500)
    private String blogMeta;
    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String content;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime createDate;

    private byte reportStatus;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "AccountID", nullable = false)
    @JsonIgnore
    private Account account;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CourseTypeID", nullable = false)
    @JsonIgnore
    private CourseType courseType;

    @OneToMany(mappedBy = "blog",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BlogReact> blogReacts;
    @OneToMany(mappedBy = "blog",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;
    @OneToMany(mappedBy = "blog",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CommentReport> commentReports;
}
