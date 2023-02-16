package com.swp.onlineLearning.DTO;

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
    @Length(min = 10, max = 70)
    private String blogName;
    @Length(min = 10, max = 150)
    private String blogMeta;
    @Length(min = 200, max = 2500)
    private String content;
    private LocalDateTime createDate;
    @Value("0")
    private byte reportStatus;

    private int courseTypeId;

}
