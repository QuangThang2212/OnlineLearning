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
    private List<ChildCommentDTO> childComment;
}
