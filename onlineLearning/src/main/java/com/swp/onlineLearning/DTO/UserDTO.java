package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String accountID;
    private String name;
    private String password;
    private String gmail;
    private String avatar;
    private Boolean banStatus;
    private LocalDateTime createAt;
    private String type;
}
