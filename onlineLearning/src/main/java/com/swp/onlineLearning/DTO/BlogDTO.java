package com.swp.onlineLearning.DTO;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.BlogReact;
import com.swp.onlineLearning.Model.Comment;
import com.swp.onlineLearning.Model.CourseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {

    private String blogID;
    private String blogName;
    private String blogMeta;
    private String content;
    private LocalDateTime createDate;
    @Value("0")
    private byte reportStatus;
    private int courseTypeId;

    private Account account;

    private CourseType courseType;

    private List<BlogReact> blogReacts;

    private List<Comment> comments;

}
