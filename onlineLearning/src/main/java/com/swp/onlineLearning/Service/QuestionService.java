package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.QuestionDTO;
import com.swp.onlineLearning.Model.Lesson;

import java.util.HashMap;

public interface QuestionService {
    HashMap<String, Object> saveQuestionAndAnswer(QuestionDTO questionDTO, Lesson returnLesson);
}
