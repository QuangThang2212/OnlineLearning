package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.Comment;
import com.swp.onlineLearning.Model.CommentReport;
import com.swp.onlineLearning.Repository.*;
import com.swp.onlineLearning.Service.CommentReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@Slf4j
public class CommentReportServiceImple implements CommentReportService {
    @Autowired
    private CommentReportRepo commentReportRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private BlogRepo blogRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Value("${comment.report.limit}")
    private int reportLimit;
    @Value("${comment.report.type.comment}")
    private String reportTypeComment;
    @Value("${comment.report.type.blog}")
    private String reportTypeBlog;
    @Override
    @Transactional
    public HashMap<String, Object> reportCommentAndBlog(String id, String type, String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        Account account = accountRepo.findByGmail(gmail);
        CommentReport commentReport;

        if(type.equals(reportTypeComment)){
            Comment comment = commentRepo.findByCommentID(id);
            if (comment == null) {
                log.error("invalid comment id");
                json.put("msg", "invalid comment id");
                return json;
            }
            commentReport = commentReportRepo.findByCommentAndAccount(comment, account);
            if (commentReport != null) {
                json.put("msg", "You had reported comment of user " + comment.getAccount().getName() + ", we will consider this action, thank you for your support with our education community");
                json.put("type", true);
                return json;
            }
            if (comment.getCommentReports().size() + 1 == reportLimit) {
                try {
                    commentRepo.deleteById(comment.getCommentID());
                } catch (Exception e) {
                    log.error("Delete comment fail \n" + e.getMessage());
                    json.put("msg", "Delete comment fail");
                    return json;
                }
                json.put("msg", "You had reported comment of user " + comment.getAccount().getName() + ", we will consider this action, thank you for your support with our education community");
                json.put("type", true);
                return json;
            } else {
                commentReport = new CommentReport();
                commentReport.setComment(comment);
                json.put("msg", "You had reported comment of user " + comment.getAccount().getName() + ", we will consider this action, thank you for your support with our education community");
            }
        }else if(type.equals(reportTypeBlog)){
            Blog blog = blogRepo.findByBlogID(id);
            if (blog == null) {
                log.error("invalid comment id");
                json.put("msg", "invalid comment id");
                return json;
            }
            commentReport = commentReportRepo.findByBlogAndAccount(blog, account);
            if (commentReport != null) {
                json.put("msg", "You had reported blog " + blog.getBlogName() + ", we will consider this action, thank you for your support with our education community");
                json.put("type", true);
                return json;
            }
            if (blog.getCommentReports().size() + 1 == reportLimit) {
                try {
                    blogRepo.deleteById(blog.getBlogID());
                } catch (Exception e) {
                    log.error("Delete blog fail \n" + e.getMessage());
                    json.put("msg", "Delete blog fail");
                    return json;
                }
                json.put("msg", "You had reported blog " + blog.getBlogName() + ", we will consider this action, thank you for your support with our education community");
                json.put("type", true);
                return json;
            } else {
                commentReport = new CommentReport();
                commentReport.setBlog(blog);
                json.put("msg", "You had reported blog " + blog.getBlogName() + ", we will consider this action, thank you for your support with our education community");
            }
        }else{
            json.put("msg", "Invalid report type");
            return json;
        }
        commentReport.setAccount(account);
        commentReport.setReportAt(LocalDateTime.now());

        try {
            commentReportRepo.save(commentReport);
        } catch (Exception e) {
            log.error("Report comment fail \n" + e.getMessage());
            json.put("msg", "Report comment fail");
            return json;
        }

        json.put("type", true);
        return json;
    }
}
