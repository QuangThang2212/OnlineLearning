package com.swp.onlineLearning.Service;

import java.util.HashMap;

public interface LessonService {
    HashMap<String, Object> getLessonForLearning(Integer courseID, Integer lessonID, String gmail);
}
