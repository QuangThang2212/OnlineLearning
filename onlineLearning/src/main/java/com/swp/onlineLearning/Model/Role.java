package com.swp.onlineLearning.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int RoleID;
    @Column(nullable = false,unique = true)
    private String name;
    @OneToMany(mappedBy = "role",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Account> accounts;
}