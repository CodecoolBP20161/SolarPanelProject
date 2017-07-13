package com.codecool.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Inverter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    private String name;

    @NotNull
    private int price;

    @NotNull
    private int capacity;


    private String description;

    public Inverter(String name, int price, int capacity, String description) {
        this.setName(name);
        this.setPrice(price);
        this.setCapacity(capacity);
        this.setDescription(description);
    }
}
