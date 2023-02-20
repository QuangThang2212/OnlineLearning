package com.swp.onlineLearning.Service;


import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.Model.Blog;

import java.util.HashMap;

public interface BlogService {
    HashMap<String, Object> save(BlogDTO blogDTO);

}
