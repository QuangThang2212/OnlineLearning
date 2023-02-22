package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.CourseType;
import com.swp.onlineLearning.Repository.*;
import com.swp.onlineLearning.Service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
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
        HashMap<String,Object> json = new HashMap<>();
        json.put("type",false);

        if (blogDTO == null){
            log.error("Not allow pass object null");
            json.put("msg", "Not allow pass object null");
            return json;
        }
        CourseType courseType = courseTypeRepo.findByCourseTypeID(blogDTO.getCourseTypeId());
        if(courseType == null){
            log.error("Type isn't exist in system");
            json.put("msg","Type isn't exist in system");
            return json;
        }

        Account account = accountRepo.findByGmail(blogDTO.getGmail());
        if (account == null){
            log.error("Account isn't exist in system");
            json.put("msg","Account isn't exist in system");
            return json;
        }

        Blog blogNameCheck = blogRepo.findByBlogName(blogDTO.getBlogName());
        if (blogNameCheck != null){
            log.error(blogDTO.getBlogName()+" Title had already exist in system");
            json.put("msg", blogDTO.getBlogName()+" Title had already exist in system");
            return json;
        }
        blogDTO.setBlogName(blogDTO.getBlogName().trim());

        blogDTO.setCreateDate(LocalDateTime.now());
        String id = blogDTO.getGmail().substring(0,2)+"Blg"+LocalDateTime.now();
        blogDTO.setBlogID(id);

        ModelMapper modelMapper = new ModelMapper();
        Blog blog = new Blog();
        modelMapper.map(blogDTO,blog);

        blog.setCourseType(courseType);
        blog.setAccount(account);

        try {
            blogRepo.save(blog);
        }catch (Exception e){
            log.error("Save Blog type with name " + blog.getBlogName()+" fail\n" + e.getMessage());
            json.put("msg", "Save Blog type with name " + blog.getBlogName()+" fail");
            return json;
        }
        log.info("Saving new Blog type with name:"+ blog.getBlogName()+" successfully");
        json.put("msg", "Saving new Blog type with name:"+ blog.getBlogName()+" successfully");
        json.replace("type",true);

        return json;
    }
}
