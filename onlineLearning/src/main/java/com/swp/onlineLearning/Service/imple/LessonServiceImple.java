package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.*;
import com.swp.onlineLearning.Model.*;
import com.swp.onlineLearning.Repository.*;
import com.swp.onlineLearning.Service.LessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class LessonServiceImple implements LessonService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private LessonPackageRepo lessonPackageRepo;
    @Autowired
    private LessonRepo lessonRepo;
    @Autowired
    private LessonTypeRepo lessonTypeRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CourseRateRepo courseRateRepo;
    @Autowired
    private QuizResultRepo quizResultRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Value("${lessonType.quiz}")
    private String typeQuiz;
    @Value("${lessonType.listening}")
    private String typeListening;

    @Override
    public HashMap<String, Object> getLessonForLearning(Integer courseID, Integer lessonID, String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        if (courseID == null || lessonID == null) {
            log.error("Invalid input courseID: " + courseID + " and lessonID: " + lessonID);
            json.put("msg", "Invalid input courseID and lessonID");
            return json;
        }
        if (gmail == null) {
            log.error("Invalid gmail with null value");
            json.put("msg", "Invalid gmail value");
            return json;
        }
        Account account = accountRepo.findByGmail(gmail);

        Course course = courseRepo.findByCourseID(courseID);
        if (course == null) {
            log.error("Course with id: " + courseID + " isn't found in system");
            json.put("msg", "Course with id: " + courseID + " isn't found in system");
            return json;
        }
        Lesson lesson = lessonRepo.findByLessonID(lessonID);
        if (lesson == null) {
            log.error("Lesson with id: " + lessonID + " isn't exist in the system");
            json.put("msg", "Lesson with id: " + lessonID + " isn't exist in the system");
            return json;
        }

        List<LessonPackageDTO> lessonPakages = new ArrayList<>();
        LessonPackageDTO lessonPackageDTO;
        LessonDTO lessonDTO;
        List<LessonDTO> lessonDTOS;
        for (LessonPackage lessonPackage : course.getLessonPackages()) {
            lessonPackageDTO = new LessonPackageDTO();
            lessonPackageDTO.setPackageID(lessonPackage.getPackageID());
            lessonPackageDTO.setPackageTitle(lessonPackage.getName());

            lessonDTOS = new ArrayList<>();
            for (Lesson lessonShow : lessonPackage.getLessons()) {
                lessonDTO = new LessonDTO();
                lessonDTO.setTitle(lessonShow.getName());
                lessonDTO.setLessonID(lessonShow.getLessonID());
                lessonDTO.setTime(lessonShow.getTime());
                lessonDTO.setType(lessonShow.getLessonType().getName());

                lessonDTOS.add(lessonDTO);
            }
            lessonPackageDTO.setNumLesson(lessonDTOS);

            lessonPakages.add(lessonPackageDTO);
        }
        CourseRate courseRate = courseRateRepo.findByCourseAndAccount(course, account);
        if((courseRate.getLesson().getLessonPackage().getPackageID()==lesson.getLessonPackage().getPackageID()
                && courseRate.getLesson().getLessonLocation()<lesson.getLessonLocation())
                || courseRate.getLesson().getLessonPackage().getPackageLocation()<lesson.getLessonPackage().getPackageLocation()){
            courseRate.setLesson(lesson);
            try{
                courseRate = courseRateRepo.saveAndFlush(courseRate);
            }catch(Exception e){
                log.error("Update learning process fail \n" +e.getMessage());
                json.put("msg", "Update learning process fail");
                return json;
            }
        }
        int currentLearningLesson = courseRate.getLesson().getLessonLocation();
        int currentLearningPackage = courseRate.getLesson().getLessonPackage().getPackageLocation();

        lessonDTO = new LessonDTO();
        lessonDTO.setLessonID(lesson.getLessonID());
        lessonDTO.setTitle(lesson.getName());
        lessonDTO.setDescription(lesson.getDescription());
        lessonDTO.setType(lesson.getLessonType().getName());
        lessonDTO.setTime(lesson.getTime());
        lessonDTO.setType(lesson.getLessonType().getName());
        lessonDTO.setLink(lesson.getLink());

        QuestionDTO questionDTO;
        List<String> answers;
        List<QuestionDTO> questionDTOS;
        if (lesson.getLessonType().getName().equals(typeQuiz)) {
            QuizResult quizResult = quizResultRepo.findByAccountAndLesson(account, lesson);
            if(quizResult!=null){
                QuizResultDTO quizResultDTO = new QuizResultDTO();
                quizResultDTO.setQuizStatus(quizResult.isStatus());
                quizResultDTO.setResult(quizResult.getResult());
                quizResultDTO.setEnrollTime(quizResult.getEnrollTime());
                lessonDTO.setQuizResultDTO(quizResultDTO);
            }
            questionDTOS = new ArrayList<>();
            for (Question question : lesson.getQuestions()) {
                questionDTO = new QuestionDTO();
                questionDTO.setQuestionID(question.getQuestionID());
                questionDTO.setTitle(question.getQuestionContent());

                answers = new ArrayList<>();
                for (Answer answer : question.getAnswers()) {
                    answers.add(answer.getAnswerContent());
                }
                questionDTO.setAnswers(answers);
                questionDTOS.add(questionDTO);
            }
            lessonDTO.setValue(questionDTOS);
        }

        if (lesson.getLessonType().getName().equals(typeListening)) {
            List<Comment> comments = commentRepo.findFatherComByLesson(lesson.getLessonID());

            List<CommentDTO> commentDTOList = new ArrayList<>();
            CommentDTO commentDTO;
            List<Comment> childComment;
            List<ChildCommentDTO> childCommentDTOS;
            ChildCommentDTO childCommentDTO;
            for(Comment comment : comments){
                childComment = commentRepo.findByParentIDAndLesson(comment, lesson);

                commentDTO = new CommentDTO();
                commentDTO.setCommentID(comment.getCommentID());
                commentDTO.setComment(comment.getComment());

                childCommentDTOS = new ArrayList<>();
                for(Comment comment1 : childComment){
                    childCommentDTO = new ChildCommentDTO();
                    childCommentDTO.setCommentID(comment1.getCommentID());
                    childCommentDTO.setComment(comment1.getComment());
                    childCommentDTO.setCommentLocation(comment1.getCommentLocation());

                    childCommentDTOS.add(childCommentDTO);
                }
                commentDTO.setChildComment(childCommentDTOS);
                commentDTOList.add(commentDTO);
            }
            lessonDTO.setComments(commentDTOList);
        }
        json.put("lessonPakages",lessonPakages);
        json.put("currentLearningLesson", currentLearningLesson);
        json.put("currentLearningPackage", currentLearningPackage);
        json.put("lesson",lesson);
        json.put("type", true);
        log.info("Get lesson with id "+lessonID);
        return json;
    }
}
