package com.swp.onlineLearning.Model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogReact implements Serializable {
    @Id
    private String blogReactID;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "BlogID", nullable = false)
    private Blog blog;
    @NotNull@NotBlank
    @Column(nullable = false)
    private String Type;
}
