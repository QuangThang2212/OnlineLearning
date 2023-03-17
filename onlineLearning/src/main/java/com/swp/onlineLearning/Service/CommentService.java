package com.swp.onlineLearning.Service;

import com.swp.onlineLearning.DTO.CommentDTO;

import java.util.HashMap;

public interface CommentService {
    HashMap<String, Object> findAllComment(String id, String type, int page);
    HashMap<String, Object> createComment(CommentDTO commentDTO, String gmail);
    HashMap<String, Object> updateComment(CommentDTO commentDTO, String gmail);
    HashMap<String, Object> deleteComment(String id, String gmail);
    HashMap<String, Object> reportComment(String id, String gmail);
}
