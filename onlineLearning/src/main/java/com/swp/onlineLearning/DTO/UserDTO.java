package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int accountID;
    @Length(min = 5, max = 50, message = "Name length must in range from 5 to 50")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Type name isn't in the right format, only allow character and number")
    private String name;
    private String password;
    @NotBlank
    @NotNull
    private String gmail;
    private String image;
    private Boolean banStatus;
    private LocalDateTime createAt;
    private String type;
    private String role;
}
