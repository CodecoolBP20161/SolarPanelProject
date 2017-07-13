package com.codecool.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Data
@Table(name = "solarpanel")
public class SolarPanel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    private String name;


   //TO DO
    private String optimalizer;

    @NotEmpty
    private int price;

    @NotEmpty
    private int capacity;

    @NotEmpty
    private String description;

    public SolarPanel(String name, int price, int capacity, String description) {
        this.setName(name);
        this.setPrice(price);
        this.setCapacity(capacity);
        this.setDescription(description);
    }
}
