package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildCommentDTO {
    private String commentID;
    private String comment;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int commentLocation;
}
