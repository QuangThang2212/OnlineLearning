package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.DTO.CourseTypeDTO;
import com.swp.onlineLearning.DTO.UserDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.CourseType;
import com.swp.onlineLearning.Repository.*;
import com.swp.onlineLearning.Service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@Transactional
public class BlogServiceImple implements BlogService {
    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private BlogRepo blogRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CourseTypeRepo courseTypeRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Override
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
    public HashMap<String, Object> update(BlogDTO blogDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        CourseType courseType = courseTypeRepo.findByCourseTypeID(blogDTO.getCourseTypeId());
        if (courseType == null) {
            log.error("Blog type with id " + blogDTO.getBlogID() + " isn't found in system");
            json.put("msg", "Blog type with id " + blogDTO.getBlogID() + " isn't found in system");
            return json;
        }
        Blog blogNameCheck = blogRepo.findByBlogName(blogDTO.getBlogName());
        if (blogNameCheck != null) {
            log.error(blogDTO.getBlogName() + " name had already exist in system, can't update");
            json.put("msg", blogDTO.getBlogName() + " name had already exist in system, can't update");
            return json;
        }
        ModelMapper modelMapper = new ModelMapper();
        CourseType updateObject = new CourseType();
        modelMapper.map(blogDTO, updateObject);
        try {
            courseTypeRepo.save(updateObject);
        } catch (Exception e) {
            log.error("Update blog type with name " + updateObject.getCourseTypeName() + " fail\n" + e.getMessage());
            json.put("msg", "Update blog type with name " + updateObject.getCourseTypeName() + " fail");
            return json;
        }

        log.info("Update new blog type with name:" + updateObject.getCourseTypeName() + " successfully");
        json.put("msg", "Update new blog type with name:" + updateObject.getCourseTypeName() + " successfully");
        json.replace("type", true);

        return json;
    }

    public HashMap<String, Object> findAllBlog(int pageNumber, int size) {
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
            blogDTO.setName(a.getAccount().getName());
            blogDTO.setImage(a.getAccount().getImage());
            blogDTO.setCreateDate(a.getCreateDate());


            blogDTOs.add(blogDTO);

        }
        json.put("numPage", totalNumber);
        json.put("blogs", blogDTOs);
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> searchByNameBlog(int pageNumber, int size, String name) {
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
            blogDTO.setName(a.getAccount().getName());
            blogDTO.setImage(a.getAccount().getImage());

            blogDTOs.add(blogDTO);
        }
        json.put("numPage", totalNumber);
        json.put("blogs", blogDTOs);
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> getBlogDetail(String id) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Blog blog = blogRepo.findByBlogID(id);
        if (blog == null) {
            log.error("blogdetail with id " + id + " isn't found in system");
            json.put("msg", "blogdetail with id " + id + " isn't found in system");
            return json;
        }
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setBlogName(blog.getBlogName());
        blogDTO.setBlogMeta(blog.getBlogMeta());
        blogDTO.setContent(blog.getContent());
        blogDTO.setName(blog.getAccount().getName());
        blogDTO.setImage(blog.getAccount().getImage());
        blogDTO.setCourseTypeName(blog.getCourseType().getCourseTypeName());

        json.put("blogDetail", blogDTO);
        json.put("type", true);
        return json;
    }


}
