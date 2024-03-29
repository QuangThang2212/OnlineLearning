package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.DTO.BlogReactDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.BlogReact;
import com.swp.onlineLearning.Model.CourseType;
import com.swp.onlineLearning.Repository.*;
import com.swp.onlineLearning.Service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class BlogServiceImple implements BlogService {
    @Autowired
    private BlogRepo blogRepo;
    @Autowired
    private CourseTypeRepo courseTypeRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private BlogReactRepo blogReactRepo;
    @Value("${role.guest}")
    private String roleGuest;
    @Override
    @Transactional
    public HashMap<String, Object> save(BlogDTO blogDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        if (blogDTO == null) {
            log.error("Not allow pass object null");
            json.put("msg", "Not allow pass object null");
            return json;
        }
        CourseType courseType = courseTypeRepo.findByCourseTypeID(blogDTO.getCourseTypeId());
        if (courseType == null) {
            log.error("Type isn't exist in system");
            json.put("msg", "Type isn't exist in system");
            return json;
        }

        Account account = accountRepo.findByGmail(blogDTO.getGmail());

        Blog blogNameCheck = blogRepo.findByBlogName(blogDTO.getBlogName());
        if (blogNameCheck != null) {
            log.error(blogDTO.getBlogName() + " Title had already exist in system");
            json.put("msg", blogDTO.getBlogName() + " Title had already exist in system");
            return json;
        }
        blogDTO.setBlogName(blogDTO.getBlogName().trim());

        blogDTO.setCreateDate(LocalDateTime.now());
        String id = blogDTO.getGmail().substring(0, 2) + "Blg" + LocalDateTime.now();
        blogDTO.setBlogID(id);

        ModelMapper modelMapper = new ModelMapper();
        Blog blog = new Blog();
        modelMapper.map(blogDTO, blog);

        blog.setCourseType(courseType);
        blog.setAccount(account);

        try {
            blogRepo.save(blog);
        } catch (Exception e) {
            log.error("Save Blog type with name " + blog.getBlogName() + " fail\n" + e.getMessage());
            json.put("msg", "Save Blog type with name " + blog.getBlogName() + " fail");
            return json;
        }
        log.info("Saving new Blog type with name:" + blog.getBlogName() + " successfully");
        json.put("msg", "Saving new Blog type with name:" + blog.getBlogName() + " successfully");
        json.replace("type", true);

        return json;
    }

    @Override
    @Transactional
    public HashMap<String, Object> update(BlogDTO blogDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        CourseType courseType = courseTypeRepo.findByCourseTypeID(blogDTO.getCourseTypeId());
        if (courseType == null) {
            log.error("Blog type with id " + blogDTO.getCourseTypeId() + " isn't found in system");
            json.put("msg", "Blog type with id " + blogDTO.getCourseTypeId() + " isn't found in system");
            return json;
        }
        Blog blogNameCheck = blogRepo.findByBlogNameForUpdate(blogDTO.getBlogName(), blogDTO.getBlogID());
        if (blogNameCheck != null) {
            log.error(blogDTO.getBlogName() + " name had already exist in system, can't update");
            json.put("msg", blogDTO.getBlogName() + " name had already exist in system, can't update");
            return json;
        }
        Blog updateObject = blogRepo.findByBlogID(blogDTO.getBlogID());
        if(updateObject==null){
            log.error("Invalid id value");
            json.put("msg", "Invalid id value");
            return json;
        }
        updateObject.setBlogName(blogDTO.getBlogName());
        updateObject.setBlogMeta(blogDTO.getBlogMeta());
        updateObject.setContent(blogDTO.getContent());
        updateObject.setCourseType(courseType);

        try {
            blogRepo.save(updateObject);
        } catch (Exception e) {
            log.error("Update blog fail\n" + e.getMessage());
            json.put("msg", "Update blog fail");
            return json;
        }

        log.info("Update successfully");
        json.put("msg", "Update successfully");
        json.replace("type", true);

        return json;
    }

    @Override
    public HashMap<String, Object> delete(String id, String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        Account account = accountRepo.findByGmail(gmail);

        Blog deleteBlog = blogRepo.findByBlogID(id);
        if(deleteBlog==null){
            log.error("Invalid id value");
            json.put("msg", "Invalid id value");
            return json;
        }
        if(deleteBlog.getAccount().getAccountID()!= account.getAccountID()){
            log.error("Invalid account");
            json.put("msg", "Invalid account");
            return json;
        }

        try {
            blogRepo.delete(deleteBlog);
        } catch (Exception e) {
            log.error("Delete blog fail\n" + e.getMessage());
            json.put("msg", "Delete blog fail");
            return json;
        }

        log.info("Delete successfully");
        json.put("msg", "Delete successfully");
        json.replace("type", true);

        return json;
    }

    @Override
    public HashMap<String, Object> findAllBlog(int pageNumber, int size, String authority) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (pageNumber < 1 || size < 1) {
            log.error("Invalid page " + pageNumber + " or size " + size);
            json.put("msg", "Invalid page " + pageNumber + " or size " + size);
            return json;
        }

        int totalNumber = blogRepo.findAll(PageRequest.of(pageNumber - 1, size)).getTotalPages();
        if (totalNumber == 0) {
            log.error("0 blog founded");
            json.put("msg", "0 blog founded for page");
            return json;
        } else if (pageNumber > totalNumber) {
            log.error("invalid page " + pageNumber);
            json.put("msg", "invalid page " + pageNumber);
            return json;
        }

        Page<Blog> blogs = blogRepo.findAll(PageRequest.of(pageNumber - 1, size));
        if (blogs.isEmpty()) {
            log.error("0 blog founded for page " + pageNumber);
            json.put("msg", "0 blog founded for page " + pageNumber);
            return json;
        }
        List<Blog> list = blogs.stream().toList();
        boolean setMark_blogStatus = false;
        Account account = new Account();
        if(!authority.equals(roleGuest)){
            setMark_blogStatus=true;
            account = accountRepo.findByGmail(authority);
        }

        BlogReact blogReact;
        List<BlogDTO> blogDTOs = new ArrayList<>();
        for (Blog a : list) {
            BlogDTO blogDTO = new BlogDTO();
            blogDTO.setBlogID(a.getBlogID());
            blogDTO.setBlogName(a.getBlogName());
            blogDTO.setBlogMeta(a.getBlogMeta());
            blogDTO.setContent(a.getContent());
            blogDTO.setCourseTypeId(a.getCourseType().getCourseTypeID());
            blogDTO.setCourseType(a.getCourseType().getCourseTypeName());
            blogDTO.setAccountID(a.getAccount().getAccountID());
            blogDTO.setCreateDate(a.getCreateDate());
            blogDTO.setName(a.getAccount().getName());
            blogDTO.setImage(a.getAccount().getImage());
            blogDTO.setCreateDate(a.getCreateDate());

            if(setMark_blogStatus){
                blogReact = blogReactRepo.findByAccountAndBlog(account, a);
                if(blogReact!=null){
                    blogDTO.setBlogReactID(blogReact.getBlogReactID());
                }
            }

            blogDTOs.add(blogDTO);

        }
        json.put("numPage", totalNumber);
        json.put("blogs", blogDTOs);
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> searchByNameBlog(int pageNumber, int size, String name, String authority) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (pageNumber < 1 || size < 1) {
            log.error("Invalid page " + pageNumber + " or size " + size);
            json.put("msg", "Invalid page " + pageNumber + " or size " + size);
            return json;
        }

        int totalNumber = blogRepo.searchByName(PageRequest.of(pageNumber - 1, size), name).getTotalPages();

        if (totalNumber == 0) {
            log.error("0 blog founded");
            json.put("msg", "0 blog founded for page");
            return json;
        } else if (pageNumber > totalNumber) {
            log.error("invalid page " + pageNumber);
            json.put("msg", "invalid page " + pageNumber);
            return json;
        }

        Page<Blog> blogs = blogRepo.searchByName(PageRequest.of(pageNumber - 1, size), name);
        if (blogs.isEmpty()) {
            log.error("0 blog founded for page " + pageNumber);
            json.put("msg", "0 blog founded for page " + pageNumber);
            return json;
        }
        List<Blog> list = blogs.stream().toList();
        boolean setMark_blogStatus = false;
        Account account = new Account();
        if(!authority.equals(roleGuest)){
            setMark_blogStatus=true;
            account = accountRepo.findByGmail(authority);
        }

        BlogReact blogReact;
        List<BlogDTO> blogDTOs = new ArrayList<>();
        Account accountUser;
        for (Blog a : list) {
            accountUser = a.getAccount();
            BlogDTO blogDTO = new BlogDTO();
            blogDTO.setBlogID(a.getBlogID());
            blogDTO.setBlogName(a.getBlogName());
            blogDTO.setBlogMeta(a.getBlogMeta());
            blogDTO.setContent(a.getContent());
            blogDTO.setCourseTypeId(a.getCourseType().getCourseTypeID());
            blogDTO.setCourseType(a.getCourseType().getCourseTypeName());
            blogDTO.setAccountID(accountUser.getAccountID());
            blogDTO.setCreateDate(a.getCreateDate());
            blogDTO.setName(accountUser.getName());
            blogDTO.setImage(accountUser.getImage());

            if(setMark_blogStatus){
                blogReact = blogReactRepo.findByAccountAndBlog(account, a);
                if(blogReact!=null){
                    blogDTO.setBlogReactID(blogReact.getBlogReactID());
                }
            }

            blogDTOs.add(blogDTO);
        }
        json.put("numPage", totalNumber);
        json.put("blogs", blogDTOs);
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> getBlogDetail(String id, String authority) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Blog blog = blogRepo.findByBlogID(id);
        if (blog == null) {
            log.error("blogdetail with id " + id + " isn't found in system");
            json.put("msg", "blogdetail with id " + id + " isn't found in system");
            return json;
        }
        CourseType courseType = blog.getCourseType();
        boolean setMark_blogStatus = false;
        Account account = new Account();
        if(!authority.equals(roleGuest)){
            setMark_blogStatus=true;
            account = accountRepo.findByGmail(authority);
        }

        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setBlogName(blog.getBlogName());
        blogDTO.setBlogMeta(blog.getBlogMeta());
        blogDTO.setContent(blog.getContent());
        blogDTO.setAccountID(blog.getAccount().getAccountID());
        blogDTO.setName(blog.getAccount().getName());
        blogDTO.setImage(blog.getAccount().getImage());
        blogDTO.setCreateDate(blog.getCreateDate());
        blogDTO.setCourseTypeId(courseType.getCourseTypeID());
        blogDTO.setCourseTypeName(courseType.getCourseTypeName());

        if(setMark_blogStatus){
            BlogReact blogReact = blogReactRepo.findByAccountAndBlog(account, blog);
            if(blogReact!=null){
                blogDTO.setBlogReactID(blogReact.getBlogReactID());
            }
        }

        json.put("blogDetail", blogDTO);
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> getOwnerBlog(String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        Account account = accountRepo.findByGmail(gmail);

        List<Blog> blogs = blogRepo.findByAccount(account);
        if(blogs.isEmpty()){
            json.put("type", true);
            return json;
        }
        BlogDTO blogDTO;
        List<BlogDTO> blogDTOS = new ArrayList<>();
        for(Blog blog: blogs){
            blogDTO = new BlogDTO();
            blogDTO.setName(blog.getBlogName());
            blogDTO.setBlogID(blog.getBlogID());
            blogDTO.setBlogMeta(blog.getBlogMeta());
            blogDTO.setBlogName(blog.getBlogName());
            blogDTO.setContent(blog.getContent());
            blogDTO.setCourseTypeId(blog.getCourseType().getCourseTypeID());
            blogDTO.setCourseTypeName(blog.getCourseType().getCourseTypeName());
            blogDTO.setAccountID(account.getAccountID());
            blogDTO.setName(account.getName());
            blogDTO.setImage(account.getImage());

            blogDTOS.add(blogDTO);
        }
        json.put("blogs", blogDTOS);
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> mark_blog(BlogReactDTO blogReactDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        BlogReact blogReact = new BlogReact();
        if (blogReactDTO.getBlogReactID() != null) {
            blogReact = blogReactRepo.findByBlogReactID(blogReactDTO.getBlogReactID());
            if (blogReact == null) {
                log.error("blogReact with id: " + blogReactDTO.getBlogReactID() + " isn't found in system");
                json.put("msg", "React blog fail");
                return json;
            }
            try {
                blogReactRepo.delete(blogReact);
            } catch (Exception e) {
                log.error("delete react blog fail\n" + e.getMessage());
                json.put("msg", "delete react blog fail");
                return json;
            }
        } else {
            Blog blog = blogRepo.findByBlogID(blogReactDTO.getBlogID());
            if (blog == null) {
                log.error("blog isn't exist in system");
                json.put("msg", "blog isn't exist in system");
                return json;
            }
            Account account = accountRepo.findByGmail(blogReactDTO.getGmail());
            if (account == null) {
                log.error("account isn't exist in system");
                json.put("msg", "account isn't exist in system");
                return json;
            }
            blogReact.setBlogReactID(LocalDateTime.now().toString());
            blogReact.setBlog(blog);
            blogReact.setAccount(account);

            try {
                BlogReact result = blogReactRepo.saveAndFlush(blogReact);
                json.put("blogReactID",result.getBlogReactID());
            } catch (Exception e) {
                log.error("Love Blog fail\n" + e.getMessage());
                json.put("msg", "Love Blog fail\n");
                return json;
            }
        }
        log.info("Un-like blog successfully\n");
        json.replace("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> getFavouriteBlog(String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        Account account = accountRepo.findByGmail(gmail);

        List<Blog> blogs = blogRepo.findFavoriteBlog(account.getAccountID());
        if(blogs.isEmpty()){
            json.put("type", true);
            return json;
        }

        boolean setMark_blogStatus = !account.getRoleUser().getName().equals(roleGuest);
        BlogDTO blogDTO;
        Account accountUser;
        List<BlogDTO> blogDTOS = new ArrayList<>();
        for(Blog blog: blogs){
            accountUser = blog.getAccount();
            blogDTO = new BlogDTO();
            blogDTO.setName(blog.getBlogName());
            blogDTO.setBlogID(blog.getBlogID());
            blogDTO.setBlogMeta(blog.getBlogMeta());
            blogDTO.setBlogName(blog.getBlogName());
            blogDTO.setContent(blog.getContent());
            blogDTO.setCourseTypeId(blog.getCourseType().getCourseTypeID());
            blogDTO.setCourseTypeName(blog.getCourseType().getCourseTypeName());
            blogDTO.setAccountID(accountUser.getAccountID());
            blogDTO.setName(accountUser.getName());
            blogDTO.setImage(accountUser.getImage());

            if(setMark_blogStatus){
                BlogReact blogReact = blogReactRepo.findByAccountAndBlog(account, blog);
                if(blogReact!=null){
                    blogDTO.setBlogReactID(blogReact.getBlogReactID());
                }
            }

            blogDTOS.add(blogDTO);
        }
        json.put("blogs", blogDTOS);
        json.put("type", true);
        return json;
    }
}
