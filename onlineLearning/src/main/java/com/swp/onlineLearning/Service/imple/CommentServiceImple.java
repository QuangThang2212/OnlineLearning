package com.swp.onlineLearning.Service.imple;

import com.swp.onlineLearning.DTO.CommentDTO;
import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Blog;
import com.swp.onlineLearning.Model.Comment;
import com.swp.onlineLearning.Model.Lesson;
import com.swp.onlineLearning.Repository.BlogRepo;
import com.swp.onlineLearning.Repository.CommentRepo;
import com.swp.onlineLearning.Repository.LessonRepo;
import com.swp.onlineLearning.Service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    @Value("${comment.type.blog}")
    private String typeBlog;
    @Value("${comment.type.lesson}")
    private String typeLesson;
    @Override
    public HashMap<String, Object> findAllComment(String id, String type) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("type", false);

        List<CommentDTO> commentDTOList = new ArrayList<>();
        CommentDTO commentDTO;
        List<Comment> childComment;
        List<CommentDTO> childCommentDTOS;
        CommentDTO childCommentDTO;
        Account account;
        if(type.equals(typeBlog)){
            List<Comment> comments = commentRepo.findFatherComByBlog(id);

            Blog blog = blogRepo.findByBlogID(id);
            if(blog==null){
                log.error("Invalid id, lesson isn't found");
                json.put("msg", "Invalid id, lesson isn't found");
                return json;
            }

            for (Comment comment : comments) {
                childComment = commentRepo.findByParentIDAndBlog(comment, blog);

                commentDTO = new CommentDTO();
                commentDTO.setCommentID(comment.getCommentID());
                commentDTO.setComment(comment.getComment());

                account = comment.getAccount();
                commentDTO.setUserID(account.getAccountID());
                commentDTO.setUserName(account.getName());
                commentDTO.setImage(account.getImage());

                childCommentDTOS = new ArrayList<>();
                for (Comment comment1 : childComment) {
                    childCommentDTO = new CommentDTO();
                    childCommentDTO.setCommentID(comment1.getCommentID());
                    childCommentDTO.setComment(comment1.getComment());
                    childCommentDTO.setCommentLocation(comment1.getCommentLocation());

                    account = comment1.getAccount();
                    childCommentDTO.setUserID(account.getAccountID());
                    childCommentDTO.setUserName(account.getName());
                    childCommentDTO.setImage(account.getImage());

                    childCommentDTOS.add(childCommentDTO);
                }
                commentDTO.setChildComment(childCommentDTOS);
                commentDTOList.add(commentDTO);
            }
        }else if (type.equals(typeLesson)){
            int lessonID;
            try{
                lessonID = Integer.parseInt(id);
            }catch (NumberFormatException e){
                log.error("Invalid lesson id, can't load comment \n"+e.getMessage());
                json.put("msg", "Invalid lesson id, can't load comment");
                return json;
            }
            List<Comment> comments = commentRepo.findFatherComByLesson(lessonID);

            Lesson lesson = lessonRepo.findByLessonID(lessonID);
            if(lesson==null){
                log.error("Invalid id, lesson isn't found");
                json.put("msg", "Invalid id, lesson isn't found");
                return json;
            }

            for (Comment comment : comments) {
                childComment = commentRepo.findByParentIDAndLesson(comment, lesson);

                commentDTO = new CommentDTO();
                commentDTO.setCommentID(comment.getCommentID());
                commentDTO.setComment(comment.getComment());

                account = comment.getAccount();
                commentDTO.setUserID(account.getAccountID());
                commentDTO.setUserName(account.getName());
                commentDTO.setImage(account.getImage());

                childCommentDTOS = new ArrayList<>();
                for (Comment comment1 : childComment) {
                    childCommentDTO = new CommentDTO();
                    childCommentDTO.setCommentID(comment1.getCommentID());
                    childCommentDTO.setComment(comment1.getComment());
                    childCommentDTO.setCommentLocation(comment1.getCommentLocation());

                    account = comment1.getAccount();
                    childCommentDTO.setUserID(account.getAccountID());
                    childCommentDTO.setUserName(account.getName());
                    childCommentDTO.setImage(account.getImage());

                    childCommentDTOS.add(childCommentDTO);
                }
                commentDTO.setChildComment(childCommentDTOS);
                commentDTOList.add(commentDTO);
            }
        }else{
            log.error("Invalid comment type("+type+"), can't load comment");
            json.put("msg", "Invalid comment type("+type+"), can't load comment");
            return json;
        }
        json.put("comments", commentDTOList);
        json.put("msg", "Get list of course successfully");
        json.put("type", true);
        return json;
    }
}
