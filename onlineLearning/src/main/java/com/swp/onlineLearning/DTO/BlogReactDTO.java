package com.swp.onlineLearning.DTO;

import com.swp.onlineLearning.Model.Account;
import com.swp.onlineLearning.Model.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogReactDTO {

    private String blogReactID;
    private int accountID;
    private String blogID;

    private String gmail;

}
