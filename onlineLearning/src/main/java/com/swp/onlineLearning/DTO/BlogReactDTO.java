package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogReactDTO {
    private String blogReactID;
    private String gmail;
    @NotNull(message = "Not allow blogID null")
    private String blogID;
}
