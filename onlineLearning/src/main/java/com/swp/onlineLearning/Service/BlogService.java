package com.swp.onlineLearning.Service;


import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.DTO.CourseTypeDTO;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.Course;

import java.util.HashMap;

public interface BlogService {
    HashMap<String, Object> save(BlogDTO blogDTO);
    HashMap<String, Object> update(BlogDTO blogDTO);
}
