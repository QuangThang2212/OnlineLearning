package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.QuizSubmitDTO;

import java.util.HashMap;

public interface LessonService {
    HashMap<String, Object> getLessonForLearning(Integer courseID, Integer lessonID, String gmail);
    HashMap<String, Object> calSubmitQuiz(QuizSubmitDTO submitDTO);
}
