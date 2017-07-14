package com.codecool.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "solarpanel")
public class SolarPanel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotEmpty
    private String name;

    @NotEmpty
    private String optimalizer;

    @NotNull
    private Integer price;

    @NotNull
    private Double vat;

    @NotNull
    private Integer capacity;

    private String description;

    public SolarPanel(String name, String optimalizer, Integer price, Double vat, Integer capacity, String description) {
        this.setName(name);
        this.setOptimalizer(optimalizer);
        this.setPrice(price);
        this.setVat(vat);
        this.setCapacity(capacity);
        this.setDescription(description);
    }

    public SolarPanel() {
    }
}
