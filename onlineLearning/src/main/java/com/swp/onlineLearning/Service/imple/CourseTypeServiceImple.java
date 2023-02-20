package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.CourseTypeDTO;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseType;
import com.swp.onlineLearning.Repository.BlogRepo;
import com.swp.onlineLearning.Repository.CourseRepo;
import com.swp.onlineLearning.Repository.CourseTypeRepo;
import com.swp.onlineLearning.Service.CourseTypeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
@Slf4j
@Service
public class CourseTypeServiceImple implements CourseTypeService {
    @Autowired
    private CourseTypeRepo courseTypeRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private BlogRepo blogRepo;
    @Override
    public HashMap<String, Object> save(CourseTypeDTO courseTypeDTO){
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);
        if(courseTypeDTO == null ){
            log.error("Not allow pass object null");
            json.put("msg", "Not allow pass object null");
            return json;
        }

        CourseType courseTypeCheck = courseTypeRepo.findByCourseTypeName(courseTypeDTO.getCourseTypeName());
        if(courseTypeCheck != null ){
            log.error(courseTypeDTO.getCourseTypeName()+" had already exist in system");
            json.put("msg", courseTypeDTO.getCourseTypeName()+" had already exist in system");
            return json;
        }

        ModelMapper modelMapper = new ModelMapper();
        CourseType courseType = new CourseType();
        modelMapper.map(courseTypeDTO, courseType);

        try {
            courseTypeRepo.save(courseType);
        }catch (Exception e){
            log.error("Save course type with name " + courseType.getCourseTypeName()+" fail\n" + e.getMessage());
            json.put("msg", "Save course type with name " + courseType.getCourseTypeName()+" fail");
            return json;
        }

        log.info("Saving new course type with name:"+ courseType.getCourseTypeName()+" successfully");
        json.put("msg", "Saving new course type with name:"+ courseType.getCourseTypeName()+" successfully");
        json.replace("type",true);

        return json;
    }

    @Override
    public HashMap<String, Object> update(CourseTypeDTO courseTypeDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);

        CourseType courseType = courseTypeRepo.findByCourseTypeID(courseTypeDTO.getCourseTypeID());
        if(courseType==null){
            log.error("Course type with id "+courseTypeDTO.getCourseTypeID()+" isn't found in system");
            json.put("msg", "Course type with id "+courseTypeDTO.getCourseTypeID()+" isn't found in system");
            return json;
        }
        CourseType courseTypeCheck = courseTypeRepo.findByCourseTypeName(courseTypeDTO.getCourseTypeName());
        if(courseTypeCheck != null ){
            log.error(courseTypeDTO.getCourseTypeName()+" name had already exist in system, can't update");
            json.put("msg", courseTypeDTO.getCourseTypeName()+" name had already exist in system, can't update");
            return json;
        }
        ModelMapper modelMapper = new ModelMapper();
        CourseType updateObject = new CourseType();
        modelMapper.map(courseTypeDTO, updateObject);
        try {
            courseTypeRepo.save(updateObject);
        }catch (Exception e){
            log.error("Update course type with name " + updateObject.getCourseTypeName()+" fail\n" + e.getMessage());
            json.put("msg", "Update course type with name " + updateObject.getCourseTypeName()+" fail");
            return json;
        }

        log.info("Update new course type with name:"+ updateObject.getCourseTypeName()+" successfully");
        json.put("msg", "Update new course type with name:"+ updateObject.getCourseTypeName()+" successfully");
        json.replace("type",true);

        return json;
    }

    @Override
    public HashMap<String, Object> delete(int id) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);

        CourseType courseType = courseTypeRepo.findByCourseTypeID(id);
        if(courseType==null){
            log.error("Course type with id "+id+" isn't found in system");
            json.put("msg", "Course type with id "+id+" isn't found in system");
            return json;
        }
        List<Course> courses= courseRepo.findByCourseType(courseType);
        if(courses.size() >0 ){
            log.error(courseType.getCourseTypeName()+" is used by other course on the system, can't delete");
            json.put("msg", courseType.getCourseTypeName()+" is used by other course on the system, can't delete");
            return json;
        }
        List<Blog> blogs= blogRepo.findByCourseType(courseType);
        if(blogs.size() >0 ){
            log.error(courseType.getCourseTypeName()+" is used by other blog on the system, can't delete");
            json.put("msg", courseType.getCourseTypeName()+" is used by other blog on the system, can't delete");
            return json;
        }
        try {
            courseTypeRepo.delete(courseType);
        }catch (Exception e){
            log.error("Delete course type with name " + courseType.getCourseTypeName()+" fail\n" + e.getMessage());
            json.put("msg", "Delete course type with name " + courseType.getCourseTypeName()+" fail");
            return json;
        }

        log.info("Delete new course type with name:"+ courseType.getCourseTypeName()+" successfully");
        json.put("msg", "Delete new course type with name:"+ courseType.getCourseTypeName()+" successfully");
        json.replace("type",true);

        return json;
    }

    @Override
    public HashMap<String, Object> findAll() {
        HashMap<String, Object> json = new HashMap<>();
        List<CourseType> courseTypeList = courseTypeRepo.findAll();
        json.put("types", courseTypeList);
        return json;
    }
}
