package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.BlogDTO;
import com.swp.onlineLearning.DTO.CourseTypeDTO;
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

import javax.transaction.Transactional;
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

    @Override
    public HashMap<String, Object> update(BlogDTO blogDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);

        CourseType courseType = courseTypeRepo.findByCourseTypeID(blogDTO.getCourseTypeId());
        if(courseType==null){
            log.error("Blog type with id "+blogDTO.getBlogID()+" isn't found in system");
            json.put("msg", "Blog type with id "+blogDTO.getBlogID()+" isn't found in system");
            return json;
        }
        Blog blogNameCheck = blogRepo.findByBlogName(blogDTO.getBlogName());
        if(blogNameCheck != null ){
            log.error(blogDTO.getBlogName()+" name had already exist in system, can't update");
            json.put("msg", blogDTO.getBlogName()+" name had already exist in system, can't update");
            return json;
        }
        ModelMapper modelMapper = new ModelMapper();
        CourseType updateObject = new CourseType();
        modelMapper.map(blogDTO, updateObject);
        try {
            courseTypeRepo.save(updateObject);
        }catch (Exception e){
            log.error("Update blog type with name " + updateObject.getCourseTypeName()+" fail\n" + e.getMessage());
            json.put("msg", "Update blog type with name " + updateObject.getCourseTypeName()+" fail");
            return json;
        }

        log.info("Update new blog type with name:"+ updateObject.getCourseTypeName()+" successfully");
        json.put("msg", "Update new blog type with name:"+ updateObject.getCourseTypeName()+" successfully");
        json.replace("type",true);

        return json;
    }
}
