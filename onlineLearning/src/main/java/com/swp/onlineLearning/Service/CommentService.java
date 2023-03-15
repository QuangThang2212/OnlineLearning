package com.swp.onlineLearning.Service;

import java.util.HashMap;

public interface CommentService {
    HashMap<String, Object> findAllComment(String id, String type);
}
