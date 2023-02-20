package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.CourseDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Course;
import com.swp.onlineLearning.Model.CourseType;
import com.swp.onlineLearning.Model.RoleUser;
import com.swp.onlineLearning.Repository.AccountRepo;
import com.swp.onlineLearning.Repository.CourseRepo;
import com.swp.onlineLearning.Repository.CourseTypeRepo;
import com.swp.onlineLearning.Repository.RoleRepo;
import com.swp.onlineLearning.Service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
@Slf4j
public class CourseServiceImple implements CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CourseTypeRepo courseTypeRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Value("${role.courseExpert}")
    private String roleCourseExpert;
    @Override
    public HashMap<String, Object> getHomepageInfor() {
        HashMap<String, Object> json = new HashMap<>();

        json.put("PopularCourse", courseRepo.findTop8PopularCourse());
        json.put("FreePopularCourse", courseRepo.findTop8FreePopularCourse());
        json.put("NewestCourse", courseRepo.findTop8NewestCourse());
        json.put("FamousPaidCourses", courseRepo.findTop8FamousPaidCourses());

        return json;
    }

    @Override
    public Course findById(String courseID) {
        return null;
    }

    @Override
    public HashMap<String, Object> save(CourseDTO courseDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);
        if(courseDTO == null ){
            log.error("Not allow pass object null");
            json.put("msg", "Not allow pass object null");
            return json;
        }
        Account account = accountRepo.findByAccountIDAndRoleUser(courseDTO.getAccountID(),roleCourseExpert);
        if(account == null){
            log.error("Account isn't exist in the system or isn't have authority to assign");
            json.put("msg", "Account isn't exist in the system or isn't have authority to assign");
            return json;
        }
        CourseType courseType = courseTypeRepo.findByCourseTypeID(courseDTO.getCourseTypeID());
        if(courseType==null){
            log.error("Course type isn't exist in the system");
            json.put("msg", "Account isn't exist in the system");
            return json;
        }
        Course newCourse = new Course();
        ModelMapper modelMapper = new ModelMapper();
        Optional<Course> courseResult = courseRepo.findByCourseName(courseDTO.getCourseName());
        if(courseResult.isPresent()){
            newCourse = courseResult.get();
            newCourse.setCourseType(courseType);
            newCourse.setExpertID(account);
            newCourse.setPrice(courseDTO.getPrice());
            newCourse.setImage(courseDTO.getImage());
            newCourse.setCourseName(courseDTO.getCourseName());
            newCourse.setDescription(courseDTO.getDescription());
        } else {
            courseDTO.setCreateDate(LocalDateTime.now());
            modelMapper.map(courseDTO, newCourse);

            newCourse.setExpertID(account);
            newCourse.setCourseType(courseType);
        }

        try {
            courseRepo.save(newCourse);
        }catch (Exception e){
            log.error("Save course with name " + newCourse.getCourseName()+" fail\n" + e.getMessage());
            json.put("msg", "Save course with name " + newCourse.getCourseName()+" fail");
            return json;
        }

        log.info("Save course with name " + newCourse.getCourseName()+" successfully");
        json.put("msg", "Save course with name " + newCourse.getCourseName()+" successfully");
        json.replace("type",true);
        return json;
    }

    @Override
    public Course update(Course course) {
        return null;
    }

    @Override
    public Course Delete(Course course) {
        return null;
    }
}
