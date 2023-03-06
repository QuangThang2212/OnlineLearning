package com.swp.onlineLearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDTO {
    private Integer voucherID;
    @Length(min = 5, max = 50, message = "Name length must in range from 5 to 50")
    private String name;
    @Length(min = 20, max = 100, message = "Description length must in range from 20 to 100")
    private String description;
    @Range(min = 0, message = "Amount must greater than 0")
    private double amount;
    private boolean status;
    @NotNull(message = "Apply day isn't allow null value")
    @Range(min = 0, message = "Apply day must greater than 0")
    private float startApply;
    @NotNull(message = "Duration of voucher isn't allow null value")
    @Range(min = 0, message = "Duration of voucher must greater than 0")
    private float duration;
    @NotNull
    @NotBlank
    private String type;
    private Integer courseID;
    private Integer courseTypeID;
}
