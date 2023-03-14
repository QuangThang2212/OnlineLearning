package com.swp.onlineLearning.Service;


import com.swp.onlineLearning.DTO.BlogDTO;


import java.util.HashMap;


public interface BlogService {
    HashMap<String, Object> save(BlogDTO blogDTO);
    HashMap<String, Object> update(BlogDTO blogDTO);
    HashMap<String, Object> findAllBlog(int pageNumber, int size);
    HashMap<String, Object> searchByNameBlog(int pageNumber, int size,String name);
    HashMap<String, Object> getBlogDetail(String id);
    HashMap<String, Object> findAll();

}

