package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String commentID;
    @NotNull(message = "Not allow comment has null value")
    @NotBlank(message = "Not allow comment blank")
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<CommentDTO> childComment;

    private String parentID;
    private int accountID;
    private String blogID;
    private int lessonID;

    private int userID;
    private String userName;
    private String image;

    private String type;
}
