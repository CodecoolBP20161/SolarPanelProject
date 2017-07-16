package com.codecool.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public abstract class Item {

    @NotEmpty
    String name;

    String description;

    @NotNull
    BigDecimal price;

    public Item(String name, String description, BigDecimal price){
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
    }
    public Item(){}
}
