package com.swp.onlineLearning.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleID;
    @Column(nullable = false,unique = true)
    private String name;
    @OneToMany(mappedBy = "role",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Account> accounts;
}
