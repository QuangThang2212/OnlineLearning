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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
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
    public HashMap<String, Object> save(BlogReactDTO blogReactDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        Boolean markBlog =false;

        if (blogReactDTO == null) {
            log.error("Not allow pass object null");
            json.put("msg", "Not allow pass object null");
            return json;
        }

        BlogReact blogReact = new BlogReact();
        System.out.println(blogReactDTO.getBlogReactID());
        if (blogReactDTO.getBlogReactID() != null) {

            blogReact = blogReactRepo.findByBlogReactID(blogReact.getBlogReactID());
            if (blogReact == null) {
                log.error("blogReact with id: " + blogReactDTO.getBlogReactID() + " isn't found in system");
                json.put("msg", "blogReact with id: " + blogReactDTO.getBlogReactID() + " isn't found in system");
                return json;
            }
            blogReactDTO.setAccountID(blogReact.getAccount().getAccountID());
            blogReactDTO.setBlogID(blogReact.getBlog().getBlogID());
            try {
                markBlog=true;
               blogReactRepo.delete(blogReact);

            } catch (Exception e) {
                log.error("delete react blog fail\n" + e.getMessage());
                json.put("msg", "delete react blog fail");
                return json;
            }
            log.info("Not Love Blog successfully\n");
            json.put("msg", "Not Love Blog successfully\n");
            json.replace("type", true);
        } else {
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
            String id = blogReactDTO.getGmail().substring(0, 2) + "Blg" + LocalDateTime.now();
            blogReact.setBlogReactID(id);

            blogReactDTO.setBlogID(blog.getBlogID());
            blogReactDTO.setAccountID(account.getAccountID());
//            ModelMapper modelMapper = new ModelMapper();
//            modelMapper.map(blogReactDTO, blogReact);
            blogReact.setBlog(blog);
            blogReact.setAccount(account);

            try {
                markBlog=false;
            BlogReact result = blogReactRepo.save(blogReact);
             json.put("id",result.getBlogReactID());

            } catch (Exception e) {
                log.error("Love Blog fail\n" + e.getMessage());
                json.put("msg", "Love Blog fail\n");
                return json;
            }
            log.info("Love Blog successfully\n");
            json.put("msg", "Love Blog successfully\n");

            json.replace("type", true);
        }

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
