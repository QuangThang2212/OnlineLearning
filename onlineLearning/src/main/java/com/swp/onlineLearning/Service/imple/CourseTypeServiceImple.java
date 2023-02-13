package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.CourseTypeDTO;
import com.swp.onlineLearning.Model.CourseType;
import com.swp.onlineLearning.Repository.CourseTypeRepo;
import com.swp.onlineLearning.Service.CourseTypeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class CourseTypeServiceImple implements CourseTypeService {
    @Autowired
    private CourseTypeRepo courseTypeRepo;
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
    public HashMap<String, Object> findAll() {
        HashMap<String, Object> json = new HashMap<>();
        List<CourseType> courseTypeList = courseTypeRepo.findAll();
        System.out.println(courseTypeList);
        json.put("types", courseTypeList);
        return json;
    }
}
