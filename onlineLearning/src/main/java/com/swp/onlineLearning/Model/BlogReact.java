package com.swp.onlineLearning.Model;

import javax.persistence.*;
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
    @ManyToOne
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;
    @ManyToOne
    @JoinColumn(name = "BlogID", nullable = false)
    private Blog blog;
}
