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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private LessonPackageRepo lessonPackageRepo;
    @Autowired
    private LessonRepo lessonRepo;
    @Autowired
    private LessonTypeRepo lessonTypeRepo;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private AnswerRepo answerRepo;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CourseRateRepo courseRateRepo;
    @Autowired
    private VoucherRepo voucherRepo;
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private CourseFilterRepo courseFilterRepo;
    @Value("${lessonType.quiz}")
    private String typeQuiz;
    @Value("${lessonType.listening}")
    private String typeListening;
    @Value("${role.courseExpert}")
    private String roleCourseExpert;
    @Value("${role.guest}")
    private String roleGuest;
    @Value("${role.admin}")
    private String roleAdmin;
    @Value("${role.user}")
    private String roleUser;
    @Value("${voucher.price.apply.limit}")
    private double priceLimit;

    private List<CourseDTO> getListCourseDTO(List<Course> courses, boolean allowAccess) {
        List<CourseDTO> courseDTOS = new ArrayList<>();
        for (Course a : courses) {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setCourseID(a.getCourseID());
            courseDTO.setCourseName(a.getCourseName());
            courseDTO.setPrice(a.getPrice());
            courseDTO.setNumberOfEnroll(a.getNumberOfEnroll());
            courseDTO.setImage(a.getImage());
            courseDTO.setStarRated(a.getStarRated());
            courseDTO.setTypeName(a.getCourseType().getCourseTypeName());

            if (allowAccess) {
                courseDTO.setStatus(a.isStatus());
                courseDTO.setCourseExpert(a.getExpertID());
                courseDTOS.add(courseDTO);
                continue;
            }
            if (a.isStatus()) {
                courseDTOS.add(courseDTO);
            }
        }
        return courseDTOS;
    }

    @Override
    public HashMap<String, Object> getHomepageInfor(String authority) {
        HashMap<String, Object> json = new HashMap<>();
        boolean allowAccess = false;
        if (!authority.equals(roleGuest)) {
            Account account = accountRepo.findByGmail(authority);
            String roleAccess = account.getRoleUser().getName();
            if (roleAccess.equals(roleCourseExpert) || roleAccess.equals(roleAdmin)) {
                allowAccess = true;
            }
        }
        List<Course> courses;

        courses = courseRepo.findTop8PopularCourse();
        if (courses.isEmpty()) {
            log.error("0 Popular Course founded");
        }
        List<CourseDTO> popularCourse = getListCourseDTO(courses, allowAccess);

        courses = courseRepo.findTop8FreePopularCourse();
        if (courses.isEmpty()) {
            log.error("0 free popular Course founded");
        }
        List<CourseDTO> freePopularCourse = getListCourseDTO(courses, allowAccess);

        courses = courseRepo.findTop8NewestCourse();
        if (courses.isEmpty()) {
            log.error("0 new Course founded");
        }
        List<CourseDTO> newestCourse = getListCourseDTO(courses, allowAccess);

        courses = courseRepo.findTop8FamousPaidCourses();
        if (courses.isEmpty()) {
            log.error("0 famous paid Course founded");
        }
        List<CourseDTO> famousPaidCourses = getListCourseDTO(courses, allowAccess);

        json.put("PopularCourse", popularCourse);
        json.put("FreePopularCourse", freePopularCourse);
        json.put("NewestCourse", newestCourse);
        json.put("FamousPaidCourses", famousPaidCourses);

        return json;
    }

    @Override
    @Transactional
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
        Course courseCheck;

        if (courseDTO.getCourseID() != null) {
            course = courseRepo.findByCourseID(courseDTO.getCourseID());
            if (course == null) {
                log.error("Course with id: " + courseDTO.getCourseID() + " isn't found in system");
                json.put("msg", "Course with id: " + courseDTO.getCourseID() + " isn't found in system");
                return json;
            }
            if (course.isStatus()) {
                log.error("This course were inactive, not allow update");
                json.put("msg", "This course were inactive, not allow update");
                return json;
            }
            courseCheck = courseRepo.findByCourseNameAndID(courseDTO.getCourseName(), courseDTO.getCourseID());
        } else {
            courseCheck = courseRepo.findByCourseName(courseDTO.getCourseName());
            course.setCreateDate(LocalDateTime.now());
        }
        if (courseCheck != null) {
            log.error("This course with name " + course.getCourseName() + " is duplicate with name of other course on system");
            json.put("msg", "This course with name " + course.getCourseName() + " is duplicate with name of other course on system \n\n please enter new name");
            return json;
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
    public HashMap<String, Object> delete(Integer id) {
        return null;
    }

    @Override
    @Transactional
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
        if (course.isStatus()) {
            log.error("Course with id " + id + " isn't allow to update course by active status");
            json.put("msg", "Course with id " + id + " isn't allow to update course by active status, set status to inactive to update course");
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
        int quizCount = 0;
        String correctAnswer;
        String anr;
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
                        quizCount++;
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
                        quizCount++;
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
                    quizCount++;

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
                        anr = "";
                        for (String ans : quesDTO.getAnswers()) {
                            if (ans.equals(anr)) {
                                continue;
                            }
                            answer = new Answer();
                            answer.setQuestion(question);
                            answer.setAnswerContent(ans.trim());
                            correctAnswer = quesDTO.getAnswers().get(quesDTO.getCorrectAnswer()).trim();
                            if (ans.trim().equals(correctAnswer)) {
                                answer.setRightAnswer(true);
                            }
                            saveAnswer.add(answer);

                            anr = ans;
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
        ArrayList<ErrorMessageDTO> errorMessageDTOS = new ArrayList<>();
        StringBuilder stringBuilder;
        ErrorMessageDTO errorMessageDTO;
        List<Lesson> deleteLesson = new ArrayList<>();
        List<LessonPackage> deleteLessonPackage = new ArrayList<>();
        if (listOfPackageDTO.getDeleteQuestion() != null) {
            jsonCheck = questionService.deleteQuestionAndAnswer(listOfPackageDTO.getDeleteQuestion());
            if (jsonCheck.get("type").equals("false")) {
                log.error(jsonCheck.get("msg").toString());
                json.put("msg", jsonCheck.get("msg").toString());
                return json;
            }
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
                if (typeQuiz.equals(lesson.getLessonType().getName())) {
                    if (!lesson.getQuizResults().isEmpty()) {
                        log.error("Lesson with id " + lesson.getLessonID() + " have user learning history, can't delete \n");
                        errorMessageDTO = new ErrorMessageDTO();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Lesson ").append(lesson.getName()).append(" (").append(lesson.getLessonID()).append(") ").append("in Topic ").append(lesson.getLessonPackage().getName());
                        errorMessageDTO.setErrorName(stringBuilder.toString());
                        errorMessageDTO.setMessage("have user learning history, can't delete");

                        errorMessageDTOS.add(errorMessageDTO);
                        deleteStatus = false;
                    } else {
                        if (quizCount > 0) {
                            quizCount--;
                        }
                        jsonCheck = questionService.deleteQuestionObjectAndAnswer(lesson.getQuestions());
                        if (jsonCheck.get("type").equals("false")) {
                            log.error(jsonCheck.get("msg").toString());
                            json.put("msg", jsonCheck.get("msg").toString());
                            return json;
                        }
                    }
                }
                if (typeListening.equals(lesson.getLessonType().getName()) && !lesson.getComments().isEmpty()) {
                    log.error("Lesson with id " + lesson.getLessonID() + " have user learning history, can't delete \n");
                    errorMessageDTO = new ErrorMessageDTO();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Lesson ").append(lesson.getName()).append(" (").append(lesson.getLessonID()).append(") ").append("in Topic ").append(lesson.getLessonPackage().getName());
                    errorMessageDTO.setErrorName(stringBuilder.toString());
                    errorMessageDTO.setMessage("have user learning history, can't delete");

                    errorMessageDTOS.add(errorMessageDTO);
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
                        errorMessageDTO = new ErrorMessageDTO();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Lesson ").append(lessonDelete.getName()).append(" (").append(lessonDelete.getLessonID()).append(") ").append("in Topic ").append(lessonDelete.getLessonPackage().getName());
                        errorMessageDTO.setErrorName(stringBuilder.toString());
                        errorMessageDTO.setMessage("have user learning history, can't delete");

                        errorMessageDTOS.add(errorMessageDTO);
                        deleteStatus = false;
                        break;
                    }
                    if (typeListening.equals(lessonDelete.getLessonType().getName()) && !lessonDelete.getComments().isEmpty()) {
                        log.error("Lesson with id " + lessonDelete.getLessonID() + " have user learning history, can't delete \n");
                        errorMessageDTO = new ErrorMessageDTO();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Lesson ").append(lessonDelete.getName()).append(" (").append(lessonDelete.getLessonID()).append(") ").append("in Topic ").append(lessonDelete.getLessonPackage().getName());
                        errorMessageDTO.setErrorName(stringBuilder.toString());
                        errorMessageDTO.setMessage("have user learning history, can't delete");

                        errorMessageDTOS.add(errorMessageDTO);
                        deleteStatus = false;
                        break;
                    }
                }
                if (deleteStatus) {
                    deleteLessonPackage.add(fkPackage);
                    deleteLesson.addAll(fkPackage.getLessons());
                    for (Lesson lesson1 : fkPackage.getLessons()) {
                        if (typeQuiz.equals(lesson1.getLessonType().getName()) && lesson1.getQuizResults().isEmpty()) {
                            if (quizCount > 0) {
                                quizCount--;
                            }
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
        course.setNumberOfQuiz(quizCount);
        try {
            if (!deleteLesson.isEmpty()) {
                lessonRepo.deleteInBatch(deleteLesson);
            }
            if (!deleteLessonPackage.isEmpty()) {
                lessonPackageRepo.deleteInBatch(deleteLessonPackage);
            }
            courseRepo.save(course);
        } catch (Exception e) {
            log.error("Delete lesson and lesson package Process fail" + e.getMessage());
            json.put("msg", "Delete Process fail");
            return json;
        }

        log.error("Update process for list of topic successfully");
        if (errorMessageDTOS.isEmpty()) {
            json.put("msg", "Update process for list of topic successfully");
        } else {
            json.put("msgProcess", errorMessageDTOS);
        }
        json.put("type", true);
        return json;
    }

    @Override
    @Transactional
    public HashMap<String, Object> changeCourseStatus(ListOfCourseDTO listOfCourseDTO) {
        HashMap<String, Object> json = new HashMap<>();
        List<Integer> listOfCourseId = listOfCourseDTO.getCourseID();
        Course course;
        List<Course> courses = new ArrayList<>();
        ArrayList<ErrorMessageDTO> errorMessageDTOS = new ArrayList<>();
        StringBuilder stringBuilder;
        ErrorMessageDTO errorMessageDTO;
        boolean status = listOfCourseDTO.isStatus();

        List<LessonPackage> lessonPackages;
        List<Lesson> lessonLis;
        boolean lessonCheck = false;
        for (int id : listOfCourseId) {
            course = courseRepo.findByCourseID(id);
            if (status) {
                lessonPackages = course.getLessonPackages();
                if (lessonPackages.size() < 2) {
                    log.error("Course with id: " + course.getCourseID() + " have only " + lessonPackages.size() + " topic, not allow public");
                    errorMessageDTO = new ErrorMessageDTO();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Course ").append(course.getCourseName()).append(" (").append(course.getCourseID()).append(")");
                    errorMessageDTO.setErrorName(stringBuilder.toString());
                    errorMessageDTO.setMessage(" have only " + lessonPackages.size() + " topic, not allow public");

                    errorMessageDTOS.add(errorMessageDTO);
                    continue;
                }
                for (LessonPackage lessonPackage : lessonPackages) {
                    lessonLis = lessonPackage.getLessons();
                    if (lessonLis.size() < 2) {
                        log.error("Topic with id: " + lessonPackage.getPackageID() + " have only " + lessonLis.size() + " lesson, not allow public");
                        errorMessageDTO = new ErrorMessageDTO();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Topic ").append(lessonPackage.getName()).append(" (").append(lessonPackage.getPackageID()).append(")").append(" in course ").append(course.getCourseName()).append(" (").append(course.getCourseID()).append(")");
                        errorMessageDTO.setErrorName(stringBuilder.toString());
                        errorMessageDTO.setMessage(" have only " + lessonLis.size() + " lesson, not allow public");

                        errorMessageDTOS.add(errorMessageDTO);
                        lessonCheck = true;
                        break;
                    }
                }
                if (lessonCheck) {
                    continue;
                }
            }
            course.setStatus(status);

            courses.add(course);
            errorMessageDTO = new ErrorMessageDTO();
            stringBuilder = new StringBuilder();
            stringBuilder.append("Course ").append(course.getCourseName()).append(" (").append(course.getCourseID()).append(")");
            errorMessageDTO.setErrorName(stringBuilder.toString());
            errorMessageDTO.setMessage(" update status successfully");

            errorMessageDTOS.add(errorMessageDTO);
        }
        try {
            courseRepo.saveAll(courses);
        } catch (Exception e) {
            log.error("Update status for list of course fail, view log " + e.getMessage());
            json.put("msg", "Update Status Fail");
            return json;
        }
        if (errorMessageDTOS.isEmpty()) {
            log.error("Update status for list of course successfully");
            json.put("msg", "Update status for list of course successfully");
        } else {
            json.put("msgProgress", errorMessageDTOS);
        }
        json.put("type", true);
        return json;
    }


    @Override
    public HashMap<String, Object> findAll(int page, int size, String role, String typeFilter, String sort, String kind, String search) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (page < 1 || size < 1) {
            log.error("Invalid page " + page + " or size " + size);
            json.put("msg", "Invalid page " + page + " or size " + size);
            return json;
        }

        boolean allowAccess = false;
        if (!role.equals(roleGuest)) {
            Account account = accountRepo.findByGmail(role);
            String roleAccess = account.getRoleUser().getName();
            if (roleAccess.equals(roleCourseExpert) || roleAccess.equals(roleAdmin)) {
                allowAccess = true;
            }
        }
        Integer type = null;
        if (!typeFilter.equals("null")) {
            try {
                type = Integer.parseInt(typeFilter);
            } catch (NumberFormatException e) {
                log.info("Type value null, not allow filter");
            }
        }
        Boolean kindFilter = null;
        if (!kind.equals("null")) {
            try {
                kindFilter = Boolean.parseBoolean(kind);
            } catch (NumberFormatException e) {
                log.info("Type value null, not allow search");
            }
        }

        int totalNumber;
        Page<Course> courses;
        if (!search.equals("null")) {
            totalNumber = courseRepo.findAllCourseByKeyWord(search, PageRequest.of(page - 1, size)).getTotalPages();
            courses = courseRepo.findAllCourseByKeyWord(search, PageRequest.of(page - 1, size));
        } else {
            CourseFilterObjectDTO courseFilterObjectDTO = courseFilterRepo.findCourseFilter(type, sort, kindFilter, PageRequest.of(page - 1, size));
            totalNumber = courseFilterObjectDTO.getTotal();
            courses = courseFilterObjectDTO.getCourses();
        }

        if (totalNumber == 0) {
            log.error("0 course founded");
            json.put("msg", "0 course founded for page");
            return json;
        } else if (page > totalNumber) {
            log.error("invalid page " + page);
            json.put("msg", "invalid page " + page);
            return json;
        }

        if (courses.isEmpty()) {
            log.error("0 course founded for page " + page);
            json.put("msg", "0 course founded for page " + page);
            json.put("courses", null);
            json.put("numPage", totalNumber);
            return json;
        }

        List<CourseDTO> courseDTOS = getListCourseDTO(courses.stream().toList(), allowAccess);

        json.put("courses", courseDTOS);
        json.put("numPage", totalNumber);
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> findCourseByIdToUpdate(Integer id) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
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

    @Override
    public HashMap<String, Object> findAllPurchaseCourse(int page, int size, String search) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (page < 1 || size < 1) {
            log.error("Invalid page " + page + " or size " + size);
            json.put("msg", "Invalid page " + page + " or size " + size);
            return json;
        }
        int totalNumber;
        if (search != null) {
            totalNumber = courseRepo.findAllPurchaseCourseByKeyWord(priceLimit, search, PageRequest.of(page - 1, size)).getTotalPages();
        } else {
            totalNumber = courseRepo.findAllPurchaseCourse(priceLimit, PageRequest.of(page - 1, size)).getTotalPages();
        }
        if (totalNumber == 0) {
            log.error("0 Course founded");
            json.put("msg", "0 Course founded for page");
            return json;
        } else if (page > totalNumber) {
            log.error("invalid page " + page);
            json.put("msg", "invalid page " + page);
            return json;
        }
        Page<Course> courses;
        if (search != null) {
            courses = courseRepo.findAllPurchaseCourseByKeyWord(priceLimit, search, PageRequest.of(page - 1, size));
        } else {
            courses = courseRepo.findAllPurchaseCourse(priceLimit, PageRequest.of(page - 1, size));
        }
        if (courses.isEmpty()) {
            log.error("0 Course founded for page " + page);
            json.put("msg", "0 Course founded for page " + page);
            return json;
        }
        List<CourseDTO> courseDTOS = getListCourseDTO(courses.stream().toList(), true);
        json.put("courses", courseDTOS);
        json.put("numPage", totalNumber);
        json.put("type", true);

        return json;
    }

    @Override
    public HashMap<String, Object> findCourseById(String authority, Integer id) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Course course = courseRepo.findByCourseID(id);
        if (course == null) {
            log.error("Course with id " + id + " isn't exist in system");
            json.put("msg", "Course with id " + id + " isn't exist in system");
            return json;
        }
        if (!course.isStatus()) {
            log.error("Course with id " + id + " are in the inactive mode for further update, we will come back soon");
            json.put("msg", "Course with id " + id + " are in the inactive mode for further update, we will come back soon");
            return json;
        }
        boolean enrolled = false;
        if (!authority.equals(roleGuest)) {
            Account account = accountRepo.findByGmail(authority);
            if (account.getRoleUser().getName().equals(roleUser)) {
                CourseRate courseRate = courseRateRepo.findByCourseAndAccount(course, account);
                if (courseRate != null) {
                    enrolled = true;
                }
            }
        }

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseID(course.getCourseID());
        courseDTO.setCourseName(course.getCourseName());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setCourseExpertName(course.getExpertID().getName());
        courseDTO.setPrice(course.getPrice());
        courseDTO.setTypeName(course.getCourseType().getCourseTypeName());
        courseDTO.setImage(course.getImage());

        List<LessonPackage> lessonPackages = course.getLessonPackages();
        List<LessonPackageDTO> lessonPackageDTOS = new ArrayList<>();
        LessonPackageDTO lessonPackageDTO;
        LessonDTO lessonDTO;
        List<LessonDTO> lessonDTOS;
        for (LessonPackage lessonPackage : lessonPackages) {
            lessonPackageDTO = new LessonPackageDTO();
            lessonPackageDTO.setPackageID(lessonPackage.getPackageID());
            lessonPackageDTO.setPackageTitle(lessonPackage.getName());

            lessonDTOS = new ArrayList<>();
            for (Lesson lesson : lessonPackage.getLessons()) {
                lessonDTO = new LessonDTO();
                lessonDTO.setLessonID(lesson.getLessonID());
                lessonDTO.setTitle(lesson.getName());
                lessonDTO.setTime(lesson.getTime());
                lessonDTO.setType(lesson.getLessonType().getName());

                lessonDTOS.add(lessonDTO);
            }
            lessonPackageDTO.setNumLesson(lessonDTOS);

            lessonPackageDTOS.add(lessonPackageDTO);
        }

        json.put("enrolled", enrolled);
        json.put("course", courseDTO);
        json.put("lessonPackages", lessonPackageDTOS);
        json.put("type", true);
        return json;
    }

    @Override
    @Transactional
    public HashMap<String, Object> enrollCourse(String authority, EnrollInformationDTO enrollInformationDTO) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Course course = courseRepo.findByCourseID(enrollInformationDTO.getCourseID());
        if (course == null) {
            log.error("Course with id " + enrollInformationDTO.getCourseID() + " isn't exist in system");
            json.put("msg", "Course with id " + enrollInformationDTO.getCourseID() + " isn't exist in system");
            return json;
        }
        Account account = accountRepo.findByGmail(authority);
        if (!account.getRoleUser().getName().equals(roleUser)) {
            log.error("Account with gmail " + authority + " don't have authority to enroll course");
            json.put("msg", "Account with gmail " + authority + " don't have authority to enroll course");
            return json;
        }

        Payment payment = new Payment();
        payment.setCourse(course);
        payment.setAccount(account);
        payment.setAmount(enrollInformationDTO.getPrice());
        payment.setPaymentID(account.getAccountID() + course.getCourseID() + LocalDateTime.now().toString());
        payment.setPaymentAt(LocalDateTime.now());

        if (enrollInformationDTO.getVoucherID() != null) {
            Voucher voucher = voucherRepo.findByVoucherID(enrollInformationDTO.getVoucherID());
            payment.setVoucher(voucher);
        }
        try {
            paymentRepo.save(payment);
        } catch (Exception e) {
            log.error("Enroll course " + course.getCourseName() + " process fail \n" + e.getMessage());
            json.put("msg", "Enroll course " + course.getCourseName() + " process fail");
            return json;
        }

        CourseRate courseRate = courseRateRepo.findByCourseAndAccount(course, account);
        if (courseRate != null) {
            log.error("Account with gmail " + authority + " enrolled course");
            json.put("msg", "You already enrolled course, enjoy learning");
            return json;
        }
        courseRate = new CourseRate();
        courseRate.setCourse(course);
        courseRate.setLesson(course.getLessonPackages().get(0).getLessons().get(0));
        courseRate.setAccount(account);
        courseRate.setEnrollTime(LocalDateTime.now());
        courseRate.setCourseRateID(account.getAccountID() + course.getCourseID() + LocalDateTime.now().toString());

        course.setNumberOfEnroll(course.getNumberOfEnroll() + 1);
        try {
            courseRateRepo.save(courseRate);
            courseRepo.save(course);
        } catch (Exception e) {
            log.error("Enroll course " + course.getCourseName() + " process fail \n" + e.getMessage());
            json.put("msg", "Enroll course " + course.getCourseName() + " process fail");
            return json;
        }

        log.info("Enroll course " + course.getCourseName() + " process successfully");
        json.put("msg", "Enroll course " + course.getCourseName() + " process successfully, \nEnjoy your learning process");
        json.put("type", true);
        return json;
    }
}
