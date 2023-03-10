package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String commentID;
    private String comment;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<CommentDTO> childComment;

    private int parentID;
    private int commentLocation;
    private int accountID;
    private String blogID;
    private int lessonID;

    private int userID;
    private String userName;
    private String image;


}
