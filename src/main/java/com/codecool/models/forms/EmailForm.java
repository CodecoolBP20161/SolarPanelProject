package com.codecool.models.forms;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class EmailForm {
    @NotNull
    @Min(value = 0, message = "The value must be bigger than 0")
    @Max(value = 256, message = "Value must be less than 256")
    private String name;

    @NotNull
    @Min(value = 0, message = "The value must be bigger than 0")
    @Max(value = 256, message = "Value must be less than 256")
    private String emailAddress;

    @NotNull
    @Min(value = 0, message = "The value must be bigger than 0")
    @Max(value = 256, message = "Value must be less than 256")
    private String subject;

    @NotNull
    @Min(value = 0, message = "The value must be bigger than 0")
    @Max(value = 2000, message = "Value must be less than 2000")
    private String emailMessage;


}
