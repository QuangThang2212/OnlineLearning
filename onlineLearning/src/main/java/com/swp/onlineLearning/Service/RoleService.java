package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.Model.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Optional;

@Service
public interface RoleService{
    RoleUser save(RoleUser roleUser);
    RoleUser findById(int RoleID);
}
