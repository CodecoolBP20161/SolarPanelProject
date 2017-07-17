package com.codecool.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@MappedSuperclass
@Data
public abstract class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
