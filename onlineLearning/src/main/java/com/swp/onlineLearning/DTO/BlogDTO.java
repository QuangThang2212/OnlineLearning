package com.swp.onlineLearning.DTO;

import com.swp.onlineLearning.Model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {

    private String blogID;
    @Length(min = 10, max = 200, message = "Blog name must between 10 to 200")
    private String blogName;
    @Length(min = 10, max = 400, message = "Blog meta must between 10 to 400")
    private String blogMeta;
    @Length(min = 200, message = "Blog content must greater than 200 characters")
    private String content;
    private LocalDateTime createDate;
    @Value("0")
    private byte reportStatus;
    private int courseTypeId;
    private String gmail;
    private String courseTypeName;
    private String image;
    private String name;
    private int accountID;
    private String courseType;


    }

