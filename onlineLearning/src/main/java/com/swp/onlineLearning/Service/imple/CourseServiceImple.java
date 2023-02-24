package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.*;
import com.swp.onlineLearning.Model.*;
import com.swp.onlineLearning.Repository.*;
import com.swp.onlineLearning.Service.CourseService;
import com.swp.onlineLearning.Service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private AnswerRepo answerRepo;
    @Autowired
    private QuestionService questionService;
    @Value("${lessonType.quiz}")
    private String typeQuiz;
    @Value("${role.courseExpert}")
    private String roleCourseExpert;
    @Value("${quiz.pass.condition}")
    private float passCondition;

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
        json.put("type", false);
        if (courseDTO == null) {
            log.error("Not allow pass object null");
            json.put("msg", "Not allow pass object null");
            return json;
        }
        Account account = accountRepo.findByAccountIDAndRoleUser(courseDTO.getAccountID(), roleCourseExpert);
        if (account == null) {
            log.error("Account isn't exist in the system or isn't have authority to assign");
            json.put("msg", "Account isn't exist in the system or isn't have authority to assign");
            return json;
        }
        CourseType courseType = courseTypeRepo.findByCourseTypeID(courseDTO.getCourseTypeID());
        if (courseType == null) {
            log.error("Course type isn't exist in the system");
            json.put("msg", "Account isn't exist in the system");
            return json;
        }
        Course newCourse = new Course();
        ModelMapper modelMapper = new ModelMapper();
        Optional<Course> courseResult = courseRepo.findByCourseName(courseDTO.getCourseName());
        if (courseResult.isPresent()) {
            newCourse = courseResult.get();
            if (newCourse.isStatus()) {
                log.error("Course with " + newCourse.getCourseName() + " are in active status, not allow update");
                json.put("msg", "Course with " + newCourse.getCourseName() + " are in active status, not allow update");
                return json;
            }
            newCourse.setCourseType(courseType);
            newCourse.setExpertID(account);
            newCourse.setPrice(courseDTO.getPrice());
            newCourse.setStatus(courseDTO.isStatus());
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
        } catch (Exception e) {
            log.error("Save course with name " + newCourse.getCourseName() + " fail\n" + e.getMessage());
            json.put("msg", "Save course with name " + newCourse.getCourseName() + " fail");
            return json;
        }

        log.info("Save course with name " + newCourse.getCourseName() + " successfully");
        json.put("msg", "Save course with name " + newCourse.getCourseName() + " successfully");
        json.replace("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> saveLessonPackage(ListOfPackageDTO listOfPackageDTO, int id) {
        HashMap<String, Object> json = new HashMap<>();

        json.put("type", false);
        if (listOfPackageDTO == null) {
            log.error("Not allow pass object null");
            json.put("msg", "Not allow pass object null");
            return json;
        }
        Course course = courseRepo.findByCourseID(id);
        if (course==null) {
            log.error("Course with id " + id + " isn't exist in the system");
            json.put("msg", "Course with id " + id + " isn't exist in the system");
            return json;
        }
        List<LessonPackageDTO> input = listOfPackageDTO.getLessonPakages();
        if (input.isEmpty()) {
            log.error("No topic in the list");
            json.put("msg", "No topic in the list");
            return json;
        }

        List<LessonPackage> fromDB = lessonPackageRepo.findByCourse(course);
        HashMap<Integer, LessonPackage> packageFromDB = new HashMap<>();
        HashMap<Integer, Lesson> lessonFromDB = new HashMap<>();
        HashMap<Integer, Question> questionFromDB = new HashMap<>();
        for (LessonPackage db : fromDB) {
            packageFromDB.put(db.getPackageID(), db);
            for(Lesson lesson: db.getLessons()){
                lessonFromDB.put(lesson.getLessonID(), lesson);
                if(lesson.getLessonType().getName().equals(typeQuiz)){
                    for(Question question: lesson.getQuestions()){
                        questionFromDB.put(question.getQuestionID(), question);
                    }
                }
            }
        }

        LessonPackage fkPackage;
        LessonType lessonTypeDB;
        Lesson lesson;
        Question question;
        Answer answer;
        LessonPackage returnLessonPackage;
        Lesson returnLesson;
        List<LessonPackage> savePackage = new ArrayList<>();
        List<Lesson> saveLesson = new ArrayList<>();
        List<Question> saveQuestion = new ArrayList<>();
        List<Answer> saveAnswer = new ArrayList<>();
        List<Answer> deleteAnswer = new ArrayList<>();
        HashMap<String, Object> jsonCheck;
        int packCount = 1;
        int lessCount;
        for (LessonPackageDTO in : input) {
            if (in.getPackageID() == null) {
                fkPackage = new LessonPackage();
                fkPackage.setCourse(course);
                fkPackage.setName(in.getLessonTitle());
                fkPackage.setPackageLocation(packCount);

                try{
                    returnLessonPackage = lessonPackageRepo.saveAndFlush(fkPackage);
                }catch (Exception e) {
                    log.error("Saving topics process fail \n"+e.getMessage());
                    json.put("msg", "Saving topics process fail");
                    return json;
                }

                lessCount = 1;
                for (LessonDTO lessonDTO : in.getNumLesson()) {
                    lesson = new Lesson();
                    lessonTypeDB = lessonTypeRepo.findByName(lessonDTO.getType());
                    if (lessonTypeDB == null) {
                        log.error("Type of lesson " + lessonDTO.getType() + " isn't found in this course");
                        json.put("msg", "Type of lesson " + lessonDTO.getType() + " isn't found in this course");
                        return json;
                    }

                    lesson.setName(lessonDTO.getTitle());
                    lesson.setDescription(lessonDTO.getDescription());
                    lesson.setLink(lessonDTO.getLink());
                    lesson.setTime(lessonDTO.getTime());
                    lesson.setLessonLocation(lessCount);
                    lesson.setLessonPackage(returnLessonPackage);
                    lesson.setLessonType(lessonTypeDB);

                    try{
                        returnLesson = lessonRepo.saveAndFlush(lesson);
                    }catch (Exception e) {
                        log.error("Saving topics process fail \n"+e.getMessage());
                        json.put("msg", "Saving topics process fail");
                        return json;
                    }

                    lessCount++;

                    if (lessonDTO.getType().equalsIgnoreCase(typeQuiz) && !lessonDTO.getValue().isEmpty()) {
                        for(QuestionDTO questionDTO : lessonDTO.getValue()) {
                            jsonCheck = questionService.saveQuestionAndAnswer(questionDTO, returnLesson);
                            if (jsonCheck.get("type").equals("false")) {
                                log.error(jsonCheck.get("msg").toString());
                                json.put("msg", jsonCheck.get("msg").toString());
                                return json;
                            }
                        }
                    }
                }
                continue;
            }
            fkPackage = packageFromDB.get(in.getPackageID());
            fkPackage.setName(in.getLessonTitle());
            fkPackage.setPackageLocation(packCount);
            savePackage.add(fkPackage);

            lessCount = 1;
            for(LessonDTO lesInput : in.getNumLesson()){
                if(lesInput.getLessonID()==null){
                    lesson = new Lesson();
                    lesson.setName(lesInput.getTitle());
                    lesson.setDescription(lesInput.getDescription());
                    lesson.setLink(lesInput.getLink());
                    lesson.setTime(lesInput.getTime());
                    lesson.setLessonLocation(lessCount);

                    try{
                        returnLesson = lessonRepo.saveAndFlush(lesson);
                    }catch (Exception e) {
                        log.error("Saving topics process fail \n"+e.getMessage());
                        json.put("msg", "Saving topics process fail");
                        return json;
                    }

                    if(lesInput.getType().equalsIgnoreCase(typeQuiz) && !lesInput.getValue().isEmpty()){
                        for(QuestionDTO questionDTO : lesInput.getValue()) {
                            jsonCheck = questionService.saveQuestionAndAnswer(questionDTO, returnLesson);
                            if (jsonCheck.get("type").equals("false")) {
                                log.error(jsonCheck.get("msg").toString());
                                json.put("msg", jsonCheck.get("msg").toString());
                                return json;
                            }
                        }
                    }
                    continue;
                }
                lesson = lessonFromDB.get(lesInput.getLessonID());
                lesson.setName(lesInput.getTitle());
                lesson.setDescription(lesInput.getDescription());
                lesson.setLink(lesInput.getLink());
                lesson.setTime(lesInput.getTime());
                lesson.setLessonLocation(lessCount);

                saveLesson.add(lesson);

                if(lesInput.getType().equalsIgnoreCase(typeQuiz)
                        && !lesInput.getValue().isEmpty()
                        && lesson.getLessonType().getName().equals(typeQuiz)) {

                    for(QuestionDTO quesDTO : lesInput.getValue()){
                        if(quesDTO.getQuestionID()==null){
                            jsonCheck = questionService.saveQuestionAndAnswer(quesDTO, lesson);
                            if(jsonCheck.get("type").equals("false")){
                                log.error(jsonCheck.get("msg").toString());
                                json.put("msg", jsonCheck.get("msg").toString());
                                return json;
                            }
                            continue;
                        }
                        question = questionFromDB.get(quesDTO.getQuestionID());
                        question.setQuestionContent(quesDTO.getTitle());

                        saveQuestion.add(question);
                        deleteAnswer.addAll(question.getAnswers());
                        for (String ans : quesDTO.getAnswers()){
                            answer = new Answer();
                            answer.setQuestion(question);
                            answer.setAnswerContent(ans);

                            saveAnswer.add(answer);
                        }
                    }
                }
                lessCount++;
            }
            packCount++;
        }
        try {
            if(listOfPackageDTO.getDeletePackage()!=null){
                lessonPackageRepo.deleteAllByIdInBatch(listOfPackageDTO.getDeletePackage());
            }
            if(listOfPackageDTO.getDeleteLesson()!=null){
                lessonRepo.deleteAllByIdInBatch(listOfPackageDTO.getDeleteLesson());
            }
            if(listOfPackageDTO.getDeleteQuestion()!=null){
                questionRepo.deleteAllByIdInBatch(listOfPackageDTO.getDeleteQuestion());
            }
            lessonPackageRepo.saveAll(savePackage);
            lessonRepo.saveAll(saveLesson);
            questionRepo.saveAll(saveQuestion);
            answerRepo.deleteAll(deleteAnswer);
            answerRepo.saveAll(saveAnswer);
        } catch (Exception e) {
            log.error("Update process for list of topic fail \n" + e.getMessage());
            json.put("msg", "Update process for list of topic fail");
            return json;
        }

        log.error("Update process for list of topic successfully");
        json.put("msg", "Update process for list of topic successfully");
        json.put("type", true);
        return json;
    }


    @Override
    public HashMap<String, Object> findAll(int page, int size) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (page < 1 || size < 1) {
            log.error("Invalid page " + page + " or size " + size);
            json.put("msg", "Invalid page " + page + " or size " + size);
            return json;
        }

        int totalNumber = courseRepo.findAll(PageRequest.of(page - 1, size)).getTotalPages();
        if (totalNumber == 0) {
            log.error("0 course founded");
            json.put("msg", "0 course founded for page");
            return json;
        } else if (page > totalNumber) {
            log.error("invalid page " + page);
            json.put("msg", "invalid page " + page);
            return json;
        }

        Page<Course> courses = courseRepo.findAll(PageRequest.of(page - 1, size));
        if (courses.isEmpty()) {
            log.error("0 course founded for page " + page);
            json.put("msg", "0 course founded for page " + page);
            return json;
        }
        List<Course> list = courses.stream().toList();

        List<CourseDTO> courseDTOS = new ArrayList<>();
        for (Course a : list) {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setCourseID(a.getCourseID());
            courseDTO.setCourseName(a.getCourseName());
            courseDTO.setPrice(a.getPrice());
            courseDTO.setImage(a.getImage());
            courseDTO.setStatus(a.isStatus());
            System.out.println(a.isStatus());
            courseDTO.setStarRated(0);
            courseDTO.setNumberOfEnroll(a.getNumberOfEnroll());
            courseDTO.setCourseExpert(a.getExpertID());
            courseDTO.setTypeName(a.getCourseType().getCourseTypeName());
            courseDTOS.add(courseDTO);
        }

        json.put("courses", courseDTOS);
        json.put("numPage", totalNumber);
        json.put("type", true);
        return json;
    }
}