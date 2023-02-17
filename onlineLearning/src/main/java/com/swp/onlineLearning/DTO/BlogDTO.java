package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {

    private String blogID;
    @Length(min = 10, max = 120, message = "Blog name must between 10 to 120")
    private String blogName;
    @Length(min = 10, max = 500, message = "Blog meta must between 10 to 150")
    private String blogMeta;
    @Length(min = 200, message = "Blog content must greater than 200")
    private String content;
    private LocalDateTime createDate;
    @Value("0")
    private byte reportStatus;
    private int courseTypeId;
    private String gmail;
    private String courseType;



}
