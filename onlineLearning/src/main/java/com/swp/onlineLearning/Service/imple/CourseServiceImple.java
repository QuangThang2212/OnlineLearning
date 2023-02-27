package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.*;
import com.swp.onlineLearning.Model.*;
import com.swp.onlineLearning.Repository.*;
import com.swp.onlineLearning.Service.CourseService;
import com.swp.onlineLearning.Service.QuestionService;
import lombok.extern.slf4j.Slf4j;
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
    @Value("${lessonType.listening}")
    private String typeListening;
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
        Course course = new Course();

        if (courseDTO.getCourseID() != null) {
            course = courseRepo.findByCourseID(courseDTO.getCourseID());
            if (course == null) {
                log.error("Course isn't found in system");
                json.put("msg", "Course isn't found in system");
                return json;
            }
            if (course.isStatus()) {
                log.error("This course with name " + course.getCourseName() + " is duplicate with name of other course on system");
                json.put("msg", "This course with name " + course.getCourseName() + " is duplicate with name of other course on system \n please enter new name");
                return json;
            }
        } else {
            course.setCreateDate(LocalDateTime.now());
        }
        course.setCourseType(courseType);
        course.setExpertID(account);
        course.setPrice(courseDTO.getPrice());
        course.setStatus(courseDTO.isStatus());
        course.setImage(courseDTO.getImage());
        course.setCourseName(courseDTO.getCourseName());
        course.setDescription(courseDTO.getDescription());

        try {
            Course result = courseRepo.saveAndFlush(course);
            json.put("courseID", result.getCourseID());
        } catch (Exception e) {
            log.error("Save course with name " + course.getCourseName() + " fail\n" + e.getMessage());
            json.put("msg", "Save course with name " + course.getCourseName() + " fail");
            return json;
        }

        log.info("Save course with name " + course.getCourseName() + " successfully");
        json.put("msg", "Save course with name " + course.getCourseName() + " successfully");
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
        if (course == null) {
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
            for (Lesson lesson : db.getLessons()) {
                lessonFromDB.put(lesson.getLessonID(), lesson);
                if (lesson.getLessonType().getName().equals(typeQuiz)) {
                    for (Question question : lesson.getQuestions()) {
                        questionFromDB.put(question.getQuestionID(), question);
                    }
                }
            }
        }
        for (LessonPackageDTO inputPack : input) {
            if (inputPack.getPackageTitle() == null || inputPack.getPackageTitle().trim().equals("")) {
                log.error("Not allow name of topic empty or have null value");
                json.put("msg", "Not allow name of topic empty or have null value");
                return json;
            }
            for (LessonDTO inputLesson : inputPack.getNumLesson()) {
                if (inputLesson.getTitle() == null || inputLesson.getTitle().trim().equals("")) {
                    log.error("Not allow name of lesson at topic " + inputPack.getPackageTitle() + " empty or have null value");
                    json.put("msg", "Not allow name of lesson at topic " + inputPack.getPackageTitle() + " empty or have null value");
                    return json;
                }
                if (inputLesson.getType().equals(typeQuiz)) {
                    for (QuestionDTO inputQuestion : inputLesson.getValue()) {
                        if (inputQuestion.getTitle() == null || inputQuestion.getTitle().trim().equals("")) {
                            log.error("Not allow question in " + inputLesson.getTitle() + " empty or have null value");
                            json.put("msg", "Not allow question in " + inputLesson.getTitle() + " empty or have null value");
                            return json;
                        }
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
                fkPackage.setName(in.getPackageTitle().trim());
                fkPackage.setPackageLocation(packCount);

                try {
                    returnLessonPackage = lessonPackageRepo.saveAndFlush(fkPackage);
                } catch (Exception e) {
                    log.error("Saving topics process fail \n" + e.getMessage());
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
                    lesson.setName(lessonDTO.getTitle().trim());
                    lesson.setDescription(lessonDTO.getDescription().trim());
                    lesson.setLink(lessonDTO.getLink());
                    lesson.setTime(lessonDTO.getTime());
                    lesson.setLessonLocation(lessCount);
                    lesson.setLessonPackage(returnLessonPackage);
                    lesson.setLessonType(lessonTypeDB);

                    try {
                        returnLesson = lessonRepo.saveAndFlush(lesson);
                    } catch (Exception e) {
                        log.error("Saving topics process fail \n" + e.getMessage());
                        json.put("msg", "Saving topics process fail");
                        return json;
                    }

                    lessCount++;

                    if (lessonDTO.getType().equalsIgnoreCase(typeQuiz) && !lessonDTO.getValue().isEmpty()) {
                        for (QuestionDTO questionDTO : lessonDTO.getValue()) {
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
            fkPackage.setName(in.getPackageTitle().trim());
            fkPackage.setPackageLocation(packCount);
            savePackage.add(fkPackage);

            lessCount = 1;
            for (LessonDTO lesInput : in.getNumLesson()) {
                if (lesInput.getLessonID() == null) {
                    lesson = new Lesson();
                    lessonTypeDB = lessonTypeRepo.findByName(lesInput.getType());

                    lesson.setName(lesInput.getTitle().trim());
                    lesson.setDescription(lesInput.getDescription());
                    lesson.setLink(lesInput.getLink());
                    lesson.setTime(lesInput.getTime());
                    lesson.setLessonLocation(lessCount);

                    lesson.setLessonPackage(fkPackage);
                    lesson.setLessonType(lessonTypeDB);

                    try {
                        returnLesson = lessonRepo.saveAndFlush(lesson);
                    } catch (Exception e) {
                        log.error("Saving topics process fail \n" + e.getMessage());
                        json.put("msg", "Saving topics process fail");
                        return json;
                    }

                    if (lesInput.getType().equalsIgnoreCase(typeQuiz) && !lesInput.getValue().isEmpty()) {
                        for (QuestionDTO questionDTO : lesInput.getValue()) {
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
                lesson.setName(lesInput.getTitle().trim());
                lesson.setDescription(lesInput.getDescription());
                lesson.setLink(lesInput.getLink());
                lesson.setTime(lesInput.getTime());
                lesson.setLessonLocation(lessCount);

                saveLesson.add(lesson);

                if (lesInput.getType().equalsIgnoreCase(typeQuiz)
                        && !lesInput.getValue().isEmpty()
                        && lesson.getLessonType().getName().equals(typeQuiz)) {

                    for (QuestionDTO quesDTO : lesInput.getValue()) {
                        if (quesDTO.getQuestionID() == null) {
                            jsonCheck = questionService.saveQuestionAndAnswer(quesDTO, lesson);
                            if (jsonCheck.get("type").equals("false")) {
                                log.error(jsonCheck.get("msg").toString());
                                json.put("msg", jsonCheck.get("msg").toString());
                                return json;
                            }
                            continue;
                        }
                        question = questionFromDB.get(quesDTO.getQuestionID());
                        question.setQuestionContent(quesDTO.getTitle().trim());

                        saveQuestion.add(question);
                        deleteAnswer.addAll(question.getAnswers());
                        for (String ans : quesDTO.getAnswers()) {
                            answer = new Answer();
                            answer.setQuestion(question);
                            answer.setAnswerContent(ans.trim());

                            saveAnswer.add(answer);
                        }
                    }
                }
                lessCount++;
            }
            packCount++;
        }
        try {
            lessonPackageRepo.saveAll(savePackage);
            lessonRepo.saveAll(saveLesson);
            questionRepo.saveAll(saveQuestion);
            answerRepo.deleteInBatch(deleteAnswer);
            answerRepo.saveAll(saveAnswer);
        } catch (Exception e) {
            log.error("Update process for list of topic fail \n" + e.getMessage());
            json.put("msg", "Update process for list of topic fail");
            return json;
        }


        boolean deleteStatus;
        StringBuilder msgDelete = new StringBuilder();
        List<Lesson> deleteLesson = new ArrayList<>();
        List<LessonPackage> deleteLessonPackage = new ArrayList<>();
        if (listOfPackageDTO.getDeleteQuestion() != null) {
            questionService.deleteQuestionAndAnswer(listOfPackageDTO.getDeleteQuestion());
        }
        if (listOfPackageDTO.getDeleteLesson() != null) {
            for (int lessonID : listOfPackageDTO.getDeleteLesson()) {
                deleteStatus = true;
                lesson = lessonRepo.findByLessonID(lessonID);
                if (lesson == null) {
                    log.error("Lesson with id " + lessonID + " isn't exist in system");
                    json.put("msg", "Lesson with id " + lessonID + " isn't exist in system");
                    return json;
                }
                if (typeQuiz.equals(lesson.getLessonType().getName()) && lesson.getQuizResults().isEmpty()) {
                    jsonCheck = questionService.deleteQuestionObjectAndAnswer(lesson.getQuestions());
                    if (jsonCheck.get("type").equals("false")) {
                        log.error(jsonCheck.get("msg").toString());
                        json.put("msg", jsonCheck.get("msg").toString());
                        return json;
                    }
                } else {
                    log.error("Lesson with id " + lesson.getLessonID() + " have user learning history, can't delete \n");
                    msgDelete.append("Lesson with id ").append(lesson.getLessonID()).append(" have user learning history, can't delete \n");
                    deleteStatus = false;
                }
                if (typeListening.equals(lesson.getLessonType().getName()) && !lesson.getComments().isEmpty()) {
                    log.error("Lesson with id " + lesson.getLessonID() + " have user learning history, can't delete \n");
                    msgDelete.append("Lesson with id ").append(lesson.getLessonID()).append(" have user learning history, can't delete \n");
                    deleteStatus = false;
                }
                if (deleteStatus) {
                    deleteLesson.add(lesson);
                }
            }
        }

        if (listOfPackageDTO.getDeletePackage() != null) {
            for (int packageID : listOfPackageDTO.getDeletePackage()) {
                deleteStatus = true;
                fkPackage = lessonPackageRepo.findByPackageID(packageID);
                if (fkPackage == null) {
                    log.error("lessonPackage with id " + packageID + " isn't exist in system");
                    json.put("msg", "lessonPackage with id " + packageID + " isn't exist in system");
                    return json;
                }
                for (Lesson lessonDelete : fkPackage.getLessons()) {
                    if (typeQuiz.equals(lessonDelete.getLessonType().getName()) && !lessonDelete.getQuizResults().isEmpty()) {
                        log.error("Lesson with id " + lessonDelete.getLessonID() + " have user learning history, can't delete \n");
                        msgDelete.append("Lesson with id ").append(lessonDelete.getLessonID()).append(" have user learning history, can't delete \n");
                        deleteStatus = false;
                        break;
                    }
                    if (typeListening.equals(lessonDelete.getLessonType().getName()) && !lessonDelete.getComments().isEmpty()) {
                        log.error("Lesson with id " + lessonDelete.getLessonID() + " have user learning history, can't delete \n");
                        msgDelete.append("Lesson with id ").append(lessonDelete.getLessonID()).append(" have user learning history, can't delete \n");
                        deleteStatus = false;
                        break;
                    }
                }
                if (deleteStatus) {
                    deleteLessonPackage.add(fkPackage);
                    deleteLesson.addAll(fkPackage.getLessons());
                    for(Lesson lesson1 : fkPackage.getLessons()){
                        if (typeQuiz.equals(lesson1.getLessonType().getName()) && lesson1.getQuizResults().isEmpty()) {
                            jsonCheck = questionService.deleteQuestionObjectAndAnswer(lesson1.getQuestions());
                            if (jsonCheck.get("type").equals("false")) {
                                log.error(jsonCheck.get("msg").toString());
                                json.put("msg", jsonCheck.get("msg").toString());
                                return json;
                            }
                        }
                    }
                }
            }
        }
        try {
            if(!deleteLesson.isEmpty()) {
                lessonRepo.deleteInBatch(deleteLesson);
            }
            if(!deleteLessonPackage.isEmpty()) {
                lessonPackageRepo.deleteInBatch(deleteLessonPackage);
            }
        } catch (Exception e) {
            log.error("Delete lesson and lesson package Process fail" + e.getMessage());
            json.put("msg", "Delete Process fail");
            return json;
        }

        log.error("Update process for list of topic successfully");
        json.put("msg", "Update process for list of topic successfully");
        json.put("msg", msgDelete.toString());
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

    @Override
    public HashMap<String, Object> findCourseByIdToUpdate(Integer id) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (id == null) {
            log.error("Not allow id have null value");
            json.put("msg", "Not allow id have null value");
            return json;
        }
        Course course = courseRepo.findByCourseID(id);
        if (course == null) {
            log.error("Course with id " + id + " isn't exist in system");
            json.put("msg", "Course with id " + id + " isn't exist in system");
            return json;
        }
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseID(course.getCourseID());
        courseDTO.setCourseName(course.getCourseName());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setPrice(course.getPrice());
        courseDTO.setImage(course.getImage());
        courseDTO.setStatus(course.isStatus());

        Account expert = course.getExpertID();
        UserDTO courseExpertDTO = new UserDTO();
        courseExpertDTO.setName(expert.getName());
        courseExpertDTO.setGmail(expert.getGmail());
        courseExpertDTO.setImage(expert.getImage());
        courseExpertDTO.setAccountID(expert.getAccountID());

        CourseType courseType = course.getCourseType();
        CourseTypeDTO type = new CourseTypeDTO();
        type.setCourseTypeID(courseType.getCourseTypeID());
        type.setCourseTypeName(courseType.getCourseTypeName());

        List<LessonPackage> lessonPackages = course.getLessonPackages();
        List<LessonPackageDTO> lessonPackageDTOS = new ArrayList<>();
        LessonPackageDTO lessonPackageDTO;
        LessonDTO lessonDTO;
        QuestionDTO questionDTO;
        String lessonType;
        List<String> answers;
        List<LessonDTO> lessonDTOS;
        List<QuestionDTO> questionDTOS;
        int correctAnswer;
        for (LessonPackage lessonPackage : lessonPackages) {
            lessonPackageDTO = new LessonPackageDTO();
            lessonPackageDTO.setPackageID(lessonPackage.getPackageID());
            lessonPackageDTO.setPackageTitle(lessonPackage.getName());

            lessonDTOS = new ArrayList<>();
            for (Lesson lesson : lessonPackage.getLessons()) {
                lessonDTO = new LessonDTO();
                lessonDTO.setLessonID(lesson.getLessonID());
                lessonDTO.setTitle(lesson.getName());
                lessonDTO.setDescription(lesson.getDescription());
                lessonDTO.setLink(lesson.getLink());
                lessonDTO.setTime(lesson.getTime());
                lessonType = lesson.getLessonType().getName();
                lessonDTO.setType(lessonType);

                if (lessonType.equals(typeQuiz)) {
                    questionDTOS = new ArrayList<>();
                    for (Question question : lesson.getQuestions()) {
                        questionDTO = new QuestionDTO();
                        questionDTO.setQuestionID(question.getQuestionID());
                        questionDTO.setTitle(question.getQuestionContent());

                        correctAnswer = 0;
                        answers = new ArrayList<>();
                        for (Answer answer : question.getAnswers()) {
                            answers.add(answer.getAnswerContent());
                            if (answer.isRightAnswer()) {
                                questionDTO.setCorrectAnswer(correctAnswer);
                            }
                            correctAnswer++;
                        }
                        questionDTO.setAnswers(answers);
                        questionDTOS.add(questionDTO);
                    }
                    lessonDTO.setValue(questionDTOS);
                } else {
                    lessonDTO.setValue(null);
                }
                lessonDTOS.add(lessonDTO);
            }
            lessonPackageDTO.setNumLesson(lessonDTOS);

            lessonPackageDTOS.add(lessonPackageDTO);
        }

        json.put("course", courseDTO);
        json.put("courseType", type);
        json.put("lessonPackages", lessonPackageDTOS);
        json.put("courseExpert", courseExpertDTO);
        json.put("msg", "Get course successfully");
        json.put("type", true);
        return json;
    }
}
