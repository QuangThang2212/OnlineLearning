package com.swp.onlineLearning.Service;


import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.DTO.BlogReactDTO;


import java.util.HashMap;


public interface BlogService {
    HashMap<String, Object> save(BlogDTO blogDTO);
    HashMap<String, Object> update(BlogDTO blogDTO);
    HashMap<String, Object> delete(String id, String gmail);
    HashMap<String, Object> findAllBlog(int pageNumber, int size, String authority);
    HashMap<String, Object> searchByNameBlog(int pageNumber, int size,String name);
    HashMap<String, Object> getBlogDetail(String id, String authority);
    HashMap<String, Object> getOwnerBlog(String gmail);
    HashMap<String, Object> mark_blog(BlogReactDTO blogReactDTO);
}

