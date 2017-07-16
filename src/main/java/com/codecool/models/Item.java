package com.codecool.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public abstract class Item {

    @NotEmpty
    protected String name;

    protected String description;

    @NotNull
    protected BigDecimal price;
}
