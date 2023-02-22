package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.CourseDTO;
import com.swp.onlineLearning.DTO.LessonDTO;
import com.swp.onlineLearning.DTO.LessonPackageDTO;
import com.swp.onlineLearning.DTO.ListOfPackageDTO;
import com.swp.onlineLearning.Model.*;
import com.swp.onlineLearning.Repository.*;
import com.swp.onlineLearning.Service.CourseService;
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
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class CourseServiceImple implements CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CourseTypeRepo courseTypeRepo;
    @Autowired
    private LessonPackageRepo lessonPackageRepo;
    @Autowired
    private LessonRepo lessonRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private LessonTypeRepo lessonTypeRepo;
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
            if(newCourse.isStatus()){
                log.error("Course with " + newCourse.getCourseName()+" are in active status, not allow update");
                json.put("msg", "Course with " + newCourse.getCourseName()+" are in active status, not allow update");
                return json;
            }
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
            Course result = courseRepo.saveAndFlush(newCourse);
            json.put("courseID", result.getCourseID());
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
    public HashMap<String, Object> saveLessonPackage(ListOfPackageDTO listOfPackageDTO, int id) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);
        if(listOfPackageDTO == null ){
            log.error("Not allow pass object null");
            json.put("msg", "Not allow pass object null");
            return json;
        }
        Optional<Course> course = courseRepo.findByCourseID(id);
        if(course.isEmpty()){
            log.error("Course with id "+id+" isn't exist in the system");
            json.put("msg", "Course with id "+id+" isn't exist in the system");
            return json;
        }
        Course courseExist = course.get();
        List<LessonPackageDTO> input = listOfPackageDTO.getLessonPakages();
        if(input.isEmpty()){
            log.error("No topic in the list");
            json.put("msg", "No topic in the list");
            return json;
        }
        List<LessonPackage> fromDB = lessonPackageRepo.findByCourse(courseExist);
        HashMap<String, LessonPackage> fromDBHashmap = new HashMap<>();
        for(LessonPackage db: fromDB){
            fromDBHashmap.put(db.getName().toLowerCase(),db);
        }
        List<LessonPackage> savePackage = new ArrayList<>();
        List<LessonPackage> deletePackage = new ArrayList<>();
        int i = 1;
        for(LessonPackageDTO in: input){
            LessonPackage lessonPackage = fromDBHashmap.get(in.getLessonTitle().toLowerCase());
            if(lessonPackage!=null){
                lessonPackage.setName(in.getLessonTitle());
                lessonPackage.setPackageLocation(i);
                savePackage.add(lessonPackage);
            }else{
                lessonPackage = new LessonPackage();
                lessonPackage.setName(in.getLessonTitle());
                lessonPackage.setCourse(courseExist);
                lessonPackage.setPackageLocation(i);
                savePackage.add(lessonPackage);
            }
            i++;
        }
        boolean existInDb;
        for(LessonPackage db: fromDB){
            existInDb=false;
            for(LessonPackageDTO in: input){
                if(in.getLessonTitle().equalsIgnoreCase(db.getName())){
                    existInDb=true;
                    break;
                }
            }
            if(existInDb==false){
                deletePackage.add(db);
            }
        }
        try{
            lessonPackageRepo.saveAll(savePackage);
            lessonPackageRepo.deleteAll(deletePackage);
        }catch (Exception e) {
            log.error("Update process for list of topic false \n"+e.getMessage());
            json.put("msg", "Update process for list of topic false");
            return json;
        }

        List<Lesson> saveLesson = new ArrayList<>();
        List<Lesson> deleteLesson = new ArrayList<>();
        List<LessonPackage> newFromDB = lessonPackageRepo.findByCourse(courseExist);
        HashMap<String, LessonPackage> newFromDBHashmap = new HashMap<>();
        for(LessonPackage db: newFromDB){
            newFromDBHashmap.put(db.getName().toLowerCase(),db);
        }
        for(LessonPackageDTO a: input){
            List<LessonDTO> lessons = a.getNumLesson();
            i = 1;
            Optional<LessonPackage> fkPackage = lessonPackageRepo.findByNameAndCourse(a.getLessonTitle(),course.get());
            if(fkPackage.isEmpty()){
                log.error("Topic with name "+a.getLessonTitle()+" isn't found in this course");
                json.put("msg", "Topic with name "+a.getLessonTitle()+" isn't found in this course");
                return json;
            }
            List<Lesson> lessons1 = lessonRepo.findByLessonPackage(fkPackage.get());
            HashMap<String, Lesson> stringLessonHashMap = new HashMap<>();
            for(Lesson db: lessons1){
                stringLessonHashMap.put(db.getName().toLowerCase(),db);
            }
            LessonType lessonTypeDB = null;
            for(LessonDTO b: lessons){
                Lesson lesson = stringLessonHashMap.get(b.getTitle().toLowerCase());
                lessonTypeDB = lessonTypeRepo.findByName(b.getType());
                if(lesson!=null){
                    lesson.setName(b.getTitle());
                    lesson.setDescription(b.getDescription());
                    lesson.setLink(b.getLink());
                    lesson.setLessonLocation(i);
                    saveLesson.add(lesson);
                }else{
                    lesson = new Lesson();
                    lesson.setLessonID(LocalDateTime.now().toString());
                    lesson.setLessonLocation(i);
                    lesson.setName(b.getTitle());
                    lesson.setDescription(b.getDescription());
                    lesson.setLink(b.getLink());
                    lesson.setLessonPackage(fkPackage.get());
                    lesson.setLessonType(lessonTypeDB);
                    saveLesson.add(lesson);
                }
                i++;
            }
        }

        return null;
    }

    @Override
    public Course Delete(Course course) {
        return null;
    }
    @Override
    public HashMap<String, Object> findAll(int page, int size) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type",false);
        if(page<1 || size <1){
            log.error("Invalid page "+page+" or size "+size);
            json.put("msg", "Invalid page "+page+" or size "+size);
            return json;
        }

        int totalNumber = courseRepo.findAll(PageRequest.of(page-1,size)).getTotalPages();
        if(totalNumber==0){
            log.error("0 course founded");
            json.put("msg", "0 course founded for page");
            return json;
        }else if(page>totalNumber){
            log.error("invalid page "+page);
            json.put("msg", "invalid page "+page);
            return json;
        }

        Page<Course> courses = courseRepo.findAll(PageRequest.of(page-1,size));
        if(courses.isEmpty()){
            log.error("0 course founded for page "+page);
            json.put("msg", "0 course founded for page "+page);
            return json;
        }
        List<Course> list = courses.stream().toList();

        List<CourseDTO> courseDTOS = new ArrayList<>();
        for(Course a : list){
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setCourseID(a.getCourseID());
            courseDTO.setCourseName(a.getCourseName());
            courseDTO.setPrice(a.getPrice());
            courseDTO.setStatus(a.isStatus());
            courseDTO.setStarRated(0);
            courseDTO.setNumberOfEnroll(a.getNumberOfEnroll());
            courseDTO.setCourseExpert(a.getExpertID());
            courseDTOS.add(courseDTO);
        }

        json.put("courses",courseDTOS);
        json.put("numPage",totalNumber);
        json.put("type",true);
        return json;
    }
}
