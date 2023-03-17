package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {

    private String blogID;
    @Length(min = 5, max = 100, message = "Blog name length must between 5 to 100")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Type name isn't in the right format, only allow character and number")
    private String blogName;
    @Length(max = 400, message = "Blog meta length must under 400")
    private String blogMeta;
    @Length(min = 20, message = "Blog content length must greater than 20 characters")
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

