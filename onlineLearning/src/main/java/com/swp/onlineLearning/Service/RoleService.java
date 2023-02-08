package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.Model.RoleUser;

public interface RoleService{
    RoleUser save(RoleUser roleUser);
    RoleUser findById(int RoleID);
}
