package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.CommentDTO;
import com.swp.onlineLearning.Model.*;
import com.swp.onlineLearning.Repository.*;
import com.swp.onlineLearning.Service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class CommentServiceImple implements CommentService {
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private LessonRepo lessonRepo;
    @Autowired
    private BlogRepo blogRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CommentReportRepo commentReportRepo;
    @Value("${comment.type.blog}")
    private String typeBlog;
    @Value("${comment.type.lesson}")
    private String typeLesson;
    @Value("${comment.limit}")
    private int limit;
    @Value("${comment.report.limit}")
    private int reportLimit;

    @Override
    public HashMap<String, Object> findAllComment(String id, String type, int page) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);
        if (page < 1) {
            log.error("Invalid page " + page);
            json.put("msg", "Invalid page " + page);
            return json;
        }

        List<CommentDTO> commentDTOList = new ArrayList<>();
        CommentDTO commentDTO;
        List<Comment> childComment;
        List<CommentDTO> childCommentDTOS;
        CommentDTO childCommentDTO;
        Account account;
        Page<Comment> comments;
        List<Comment> commentList;
        Blog blog = new Blog();
        Lesson lesson = new Lesson();

        if (type.equals(typeBlog)) {
            comments = commentRepo.findFatherComByBlog(id, PageRequest.of(page - 1, limit));
            if (comments.isEmpty()) {
                log.info("0 comment found");
                json.put("out", true);
                json.put("type", true);
                return json;
            }
            commentList = comments.stream().toList();
            blog = blogRepo.findByBlogID(id);
            if (blog == null) {
                log.error("Invalid id, lesson isn't found");
                json.put("msg", "Invalid id, lesson isn't found");
                return json;
            }
        } else if (type.equals(typeLesson)) {
            int lessonID;
            try {
                lessonID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                log.error("Invalid lesson id, can't load comment \n" + e.getMessage());
                json.put("msg", "Invalid lesson id, can't load comment");
                return json;
            }
            comments = commentRepo.findFatherComByLesson(lessonID, PageRequest.of(page - 1, limit));
            if (comments.isEmpty()) {
                log.info("0 comment found");
                json.put("out", true);
                json.put("type", true);
                return json;
            }
            commentList = comments.stream().toList();
            lesson = lessonRepo.findByLessonID(lessonID);
            if (lesson == null) {
                log.error("Invalid id, lesson isn't found");
                json.put("msg", "Invalid id, lesson isn't found");
                return json;
            }

        } else {
            log.error("Invalid comment type(" + type + "), can't load comment");
            json.put("msg", "Invalid comment type(" + type + "), can't load comment");
            return json;
        }

        for (Comment comment : commentList) {
            if (type.equals(typeBlog)) {
                childComment = commentRepo.findByParentIDAndBlogOrderByCreateAtDesc(comment, blog);
            } else {
                childComment = commentRepo.findByParentIDAndLessonOrderByCreateAtDesc(comment, lesson);
            }

            commentDTO = new CommentDTO();
            commentDTO.setCommentID(comment.getCommentID());
            commentDTO.setContent(comment.getComment());
            commentDTO.setCreateAt(comment.getCreateAt());

            account = comment.getAccount();
            commentDTO.setUserID(account.getAccountID());
            commentDTO.setUserName(account.getName());
            commentDTO.setImage(account.getImage());

            childCommentDTOS = new ArrayList<>();
            for (Comment comment1 : childComment) {
                childCommentDTO = new CommentDTO();
                childCommentDTO.setCommentID(comment1.getCommentID());
                childCommentDTO.setContent(comment1.getComment());
                childCommentDTO.setCreateAt(comment1.getCreateAt());

                account = comment1.getAccount();
                childCommentDTO.setUserID(account.getAccountID());
                childCommentDTO.setUserName(account.getName());
                childCommentDTO.setImage(account.getImage());

                childCommentDTOS.add(childCommentDTO);
            }
            commentDTO.setChildComment(childCommentDTOS);
            commentDTOList.add(commentDTO);
        }
        json.put("comments", commentDTOList);
        json.put("msg", "Get list of course successfully");
        json.put("out", false);
        json.put("type", true);
        return json;
    }

    @Override
    @Transactional
    public HashMap<String, Object> createComment(CommentDTO commentDTO, String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Account account = accountRepo.findByGmail(gmail);

        Comment comment = new Comment();
        comment.setComment(commentDTO.getContent());
        comment.setAccount(account);
        comment.setCreateAt(LocalDateTime.now());
        comment.setCommentID(LocalDateTime.now().toString());

        if (commentDTO.getType().equals(typeBlog)) {
            Blog blog = blogRepo.findByBlogID(commentDTO.getBlogID());
            if (blog == null) {
                log.error("Blog id(" + commentDTO.getBlogID() + ") isn't found, can't save comment");
                json.put("msg", "Blog id(" + commentDTO.getType() + ") isn't found, can't save comment");
                return json;
            }
            comment.setBlog(blog);
        } else if (commentDTO.getType().equals(typeLesson)) {
            Lesson lesson = lessonRepo.findByLessonID(commentDTO.getLessonID());
            if (lesson == null) {
                log.error("lesson id(" + commentDTO.getLessonID() + ") isn't found, can't save comment");
                json.put("msg", "lesson id(" + commentDTO.getLessonID() + ") isn't found, can't save comment");
                return json;
            }
            comment.setLesson(lesson);
        } else {
            log.error("Invalid comment type(" + commentDTO.getType() + "), can't save comment");
            json.put("msg", "Invalid comment type(" + commentDTO.getType() + "), can't save comment");
            return json;
        }
        if (commentDTO.getParentID() != null) {
            Comment commentParent = commentRepo.findByCommentID(commentDTO.getParentID());
            if (commentParent == null) {
                log.error("lesson id(" + commentDTO.getParentID() + ") isn't found, can't save comment");
                json.put("msg", "lesson id(" + commentDTO.getParentID() + ") isn't found, can't save comment");
                return json;
            }
            comment.setParentID(commentParent);
        }
        Comment commentReturn;
        try {
            commentReturn = commentRepo.saveAndFlush(comment);
        } catch (Exception e) {
            log.error("Save comment fail, please try again \n" + e.getMessage());
            json.put("msg", "Save comment fail, please try again");
            return json;
        }
        json.put("commentID", commentReturn.getCommentID());
        json.put("msg", "Save comment successfully");
        json.put("type", true);
        return json;
    }

    @Override
    @Transactional
    public HashMap<String, Object> updateComment(CommentDTO commentDTO, String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Account account = accountRepo.findByGmail(gmail);

        Comment comment = commentRepo.getById(commentDTO.getCommentID());
        if (account.getAccountID() != comment.getAccount().getAccountID()) {
            log.error("Don't have authority to update comment");
            json.put("msg", "Don't have authority to update comment");
            return json;
        }
        comment.setComment(commentDTO.getContent());
        comment.setUpdateAt(LocalDateTime.now());

        try {
            commentRepo.save(comment);
        } catch (Exception e) {
            log.error("Save comment fail, please try again \n" + e.getMessage());
            json.put("msg", "Save comment fail, please try again");
            return json;
        }
        json.put("msg", "Update comment successfully");
        json.put("type", true);
        return json;
    }

    @Override
    @Transactional
    public HashMap<String, Object> deleteComment(String id, String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Account account = accountRepo.findByGmail(gmail);

        Comment comment = commentRepo.findByCommentID(id);
        if (comment == null) {
            log.error("invalid comment id");
            json.put("msg", "invalid comment id");
            return json;
        }
        if (account.getAccountID() != comment.getAccount().getAccountID()) {
            log.error("Don't have authority to delete comment");
            json.put("msg", "Don't have authority to delete comment");
            return json;
        }
        List<Comment> comments = comment.getChildComment();
        List<CommentReport> commentReports = new ArrayList<>();
        for (Comment comment1 : comments) {
            commentReports.addAll(comment1.getCommentReports());
        }

        try {
            if (!commentReports.isEmpty()) {
                commentReportRepo.deleteInBatch(commentReports);
            }
            if (!comments.isEmpty()) {
                commentRepo.deleteInBatch(comments);
            }
            commentReportRepo.deleteInBatch(comment.getCommentReports());
            commentRepo.delete(comment);
        } catch (Exception e) {
            log.error("Delete comment fail \n" + e.getMessage());
            json.put("msg", "Don't have authority to change account");
            return json;
        }

        json.put("msg", "Delete comment successfully");
        json.put("type", true);
        return json;
    }

    @Override
    @Transactional
    public HashMap<String, Object> reportComment(String id, String gmail) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        Comment comment = commentRepo.findByCommentID(id);
        if (comment == null) {
            log.error("invalid comment id");
            json.put("msg", "invalid comment id");
            return json;
        }
        Account account = accountRepo.findByGmail(gmail);
        CommentReport commentReport = commentReportRepo.findByCommentAndAccount(comment, account);
        if (commentReport != null) {
            json.put("msg", "You had reported comment of user " + comment.getAccount().getName() + ", we will consider this action, thank you for your support with our education community");
            json.put("type", true);
            return json;
        }
        if (comment.getCommentReports().size() + 1 == reportLimit) {
            List<Comment> comments = comment.getChildComment();
            List<CommentReport> commentReports = new ArrayList<>();
            for (Comment comment1 : comments) {
                commentReports.addAll(comment1.getCommentReports());
            }
            try {
                if (!commentReports.isEmpty()) {
                    commentReportRepo.deleteInBatch(commentReports);
                }
                if (!comments.isEmpty()) {
                    commentRepo.deleteInBatch(comments);
                }
                commentReportRepo.deleteInBatch(comment.getCommentReports());
                commentRepo.delete(comment);
            } catch (Exception e) {
                log.error("Delete comment fail \n" + e.getMessage());
                json.put("msg", "Don't have authority to change account");
                return json;
            }
        } else {
            commentReport = new CommentReport();
            commentReport.setAccount(account);
            commentReport.setComment(comment);
            commentReport.setReportAt(LocalDateTime.now());

            try {
                commentReportRepo.save(commentReport);
            } catch (Exception e) {
                log.error("Report comment fail \n" + e.getMessage());
                json.put("msg", "Report comment fail");
                return json;
            }
        }
        json.put("msg", "You had reported comment of user " + comment.getAccount().getName() + ", we will consider this action, thank you for your support with our education community");
        json.put("type", true);
        return json;
    }
}
