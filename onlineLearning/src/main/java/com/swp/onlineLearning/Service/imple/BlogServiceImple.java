package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.CourseType;
import com.swp.onlineLearning.Repository.AccountRepo;
import com.swp.onlineLearning.Repository.BlogRepo;
import com.swp.onlineLearning.Repository.CourseRepo;
import com.swp.onlineLearning.Repository.CourseTypeRepo;
import com.swp.onlineLearning.Service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
@Slf4j
@Service
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

        Blog blogTypeCheck = blogRepo.findByBlogName(blogDTO.getBlogName());
        if (blogTypeCheck != null){
            log.error(blogDTO.getBlogName()+" had already exist in system");
            json.put("msg", blogDTO.getBlogName()+" had already exist in system");
            return json;
        }
        blogDTO.setCreateDate(LocalDateTime.now());
        blogDTO.setBlogID(passwordEncoder.encode(blogDTO.getBlogName()));
        CourseType courseType = courseTypeRepo.findByCourseTypeID(blogDTO.getCourseTypeId());

        if(courseType == null){
            log.error("Type not exist in system");
            json.put("msg","Type not exist in system");
            return json;
        }

//        Account account = accountRepo.findAll();
//        if(account == null){
//            log.error(blogDTO.getAccount()+"Account already exist in system");
//            json.put("msg", blogDTO.getBlogName()+" Account already exist in system");
//            return json;
//        }

        ModelMapper modelMapper = new ModelMapper();
        Blog blog = new Blog();
        modelMapper.map(blogDTO,blog);

        blog.setCourseType(courseType);

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
