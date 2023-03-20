package com.swp.onlineLearning.Service;
import java.util.HashMap;

public interface CommentReportService {
    HashMap<String, Object> reportCommentAndBlog(String id, String type, String gmail);
}