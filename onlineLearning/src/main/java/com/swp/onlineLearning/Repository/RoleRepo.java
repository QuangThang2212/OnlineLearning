package com.swp.onlineLearning.Repository;

import com.swp.onlineLearning.Model.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepo extends JpaRepository<RoleUser, Integer> {
    RoleUser save(RoleUser roleUser);
    RoleUser findById(int RoleID);
    RoleUser findByName(String name);
}

