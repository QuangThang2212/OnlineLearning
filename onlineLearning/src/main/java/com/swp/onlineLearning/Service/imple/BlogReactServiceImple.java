package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.BlogReactDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.BlogReact;
import com.swp.onlineLearning.Repository.AccountRepo;
import com.swp.onlineLearning.Repository.BlogReactRepo;
import com.swp.onlineLearning.Repository.BlogRepo;
import com.swp.onlineLearning.Service.BlogReactService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
@Slf4j
@Service
public class BlogReactServiceImple implements BlogReactService {
    @Autowired
    private BlogRepo blogRepo;
    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private BlogReactRepo blogReactRepo;
    @Override
    public HashMap<String, Object> save( String id ) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        Blog blogCheckID = blogRepo.findByBlogID(id);
        if (blogCheckID == null) {
            log.error("blogdetail with id " + id + " isn't found in system");
            json.put("msg", "blogdetail with id " + id + " isn't found in system");
            return json;
        }
        BlogReactDTO blogReactDTO = new BlogReactDTO();

        if (blogReactDTO == null) {
            log.error("Not allow pass object null");
            json.put("msg", "Not allow pass object null");
            return json;
        }
        Blog blog = blogRepo.findByBlogID(blogReactDTO.getBlogID());
        if (blog == null) {
            log.error("blog isn't exist in system");
            json.put("msg", "blog isn't exist in system");
            return json;
        }
        Account account = accountRepo.findByAccountID(blogReactDTO.getAccountID());
        if (account == null) {
            log.error("account isn't exist in system");
            json.put("msg", "account isn't exist in system");
            return json;
        }
        ModelMapper modelMapper = new ModelMapper();
        BlogReact blogReact = new BlogReact();
        modelMapper.map(blogReactDTO, blogReact);
        blogReactDTO.setBlogID(blog.getBlogID());
        blogReactDTO.setAccountID(account.getAccountID());
        try {
        blogReactRepo.save(blogReact);

        } catch (Exception e) {
            log.error("Save Blog type with name " + blog.getBlogName() + " fail\n" + e.getMessage());
            json.put("msg", "Save Blog type with name " + blog.getBlogName() + " fail");
            return json;
        }
        log.info("Saving Blog type with name:" + blog.getBlogName() + " successfully");
        json.put("msg", "Saving  Blog type with name:" + blog.getBlogName() + " successfully");
        json.replace("type", true);

        return json;
    }

    @Override
    public HashMap<String, Object> getBlogMark() {
        HashMap<String, Object> json = new HashMap<>();
        List<BlogReact> blogList = blogReactRepo.findAll();
        json.put("blogs", blogList);
        return json;
    }
}
