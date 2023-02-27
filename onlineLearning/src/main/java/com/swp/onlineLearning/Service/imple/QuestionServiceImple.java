package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.QuestionDTO;
import com.swp.onlineLearning.Model.Answer;
import com.swp.onlineLearning.Model.Lesson;
import com.swp.onlineLearning.Model.Question;
import com.swp.onlineLearning.Repository.AnswerRepo;
import com.swp.onlineLearning.Repository.QuestionRepo;
import com.swp.onlineLearning.Service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@Transactional
public class QuestionServiceImple implements QuestionService {
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private AnswerRepo answerRepo;

    public QuestionServiceImple() {
    }

    @Override
    public HashMap<String, Object> saveQuestionAndAnswer(QuestionDTO questionDTO, Lesson returnLesson) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        Answer answer;
        List<Answer> answers = new ArrayList<>();

        Question question = new Question();
        question.setLesson(returnLesson);
        question.setQuestionContent(questionDTO.getTitle().trim());

        Question returnQuestion;
        try{
            returnQuestion = questionRepo.saveAndFlush(question);
        }catch (Exception e) {
            log.error("Saving topics process fail \n"+e.getMessage());
            json.put("msg", "Saving topics process fail");
            return json;
        }
        String rightAns = questionDTO.getAnswers().get(questionDTO.getCorrectAnswer());
        for(String ans : questionDTO.getAnswers()){
            answer = new Answer();
            answer.setAnswerContent(ans);
            answer.setQuestion(returnQuestion);
            answer.setRightAnswer(rightAns.equals(ans));
            answers.add(answer);
        }
        try{
            answerRepo.saveAll(answers);
        }catch (Exception e) {
            log.error("Saving topics process fail" +e.getMessage());
            json.put("msg", "Saving topics process fail");
            return json;
        }

        json.put("msg", "Saving question success");
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> deleteQuestionAndAnswer(List<Integer> questionDelete) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        Question question;
        for (int questionID : questionDelete) {
            question = questionRepo.findById(questionID).orElse(null);
            if (question == null) {
                log.error("Question with id " + questionID + " isn't exist in system");
                json.put("msg", "Question with id " + questionID + " isn't exist in system");
                return json;
            }
            for(Answer answer : question.getAnswers()){
                try{
                    answerRepo.deleteByAnswerID(answer.getAnswerID());
                }catch(Exception e){
                    log.error("Answer with id " + answer.getAnswerID() + " delete fail \n"+e.getMessage());
                    return json;
                }
            }
            try{
                questionRepo.deleteByQuestionID(question.getQuestionID());
            }catch(Exception e){
                log.error("Question with id " + questionID + " delete fail \n"+e.getMessage());
                return json;
            }
        }
        json.put("type", true);
        return json;
    }

    @Override
    public HashMap<String, Object> deleteQuestionObjectAndAnswer(List<Question> questionDelete) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        for (Question question : questionDelete) {
            for(Answer answer : question.getAnswers()){
                try{
                    answerRepo.deleteByAnswerID(answer.getAnswerID());
                }catch(Exception e){
                    log.error("Answer with id " + answer.getAnswerID() + " delete fail \n"+e.getMessage());
                    return json;
                }
            }
            try{
                questionRepo.deleteByQuestionID(question.getQuestionID());
            }catch(Exception e){
                log.error("Question with id " + question.getAnswers() + " delete fail \n"+e.getMessage());
                json.put("msg", "Question with id " + question.getAnswers() + " delete fail");
                return json;
            }
        }
        json.put("type", true);
        return json;
    }
}
