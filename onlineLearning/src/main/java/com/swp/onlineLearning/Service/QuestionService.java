package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.QuestionDTO;
import com.swp.onlineLearning.Model.Lesson;
import com.swp.onlineLearning.Model.Question;

import java.util.HashMap;
import java.util.List;

public interface QuestionService {
    HashMap<String, Object> saveQuestionAndAnswer(QuestionDTO questionDTO, Lesson returnLesson);
    HashMap<String, Object> deleteQuestionAndAnswer( List<Integer> questionDelete);
    HashMap<String, Object> deleteQuestionObjectAndAnswer( List<Question> questionDelete);
}
