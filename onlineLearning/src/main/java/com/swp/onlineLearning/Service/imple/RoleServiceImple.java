package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.Model.RoleUser;
import com.swp.onlineLearning.Repository.RoleRepo;
import com.swp.onlineLearning.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class RoleServiceImple implements RoleService {
    @Autowired
    private RoleRepo roleRepo;
    @Override
    public RoleUser save(RoleUser roleUser) {
        if(roleUser==null){
            return null;
        }
        return roleRepo.save(roleUser);
    }

    @Override
    public RoleUser findById(int RoleID) {
        Optional<RoleUser> roleUser = roleRepo.findById(RoleID);
        return roleUser.orElse(null);
    }
}
