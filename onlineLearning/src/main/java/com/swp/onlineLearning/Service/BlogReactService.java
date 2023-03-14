package com.swp.onlineLearning.Service;


import com.swp.onlineLearning.DTO.BlogReactDTO;

import java.util.HashMap;
public interface BlogReactService {
    HashMap<String, Object> save(String id);
    HashMap<String, Object> getBlogMark();
}
