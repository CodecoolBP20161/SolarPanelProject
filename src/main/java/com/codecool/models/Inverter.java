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

    @NotEmpty
    private String brand;

    private Integer wifiModule;

    @NotEmpty
    private Integer phase;

    @NotNull
    private int price;

    @NotEmpty
    private Double vatt;

    @NotNull
    private int capacity;

    private String description;


    public Inverter(String name, String brand, Integer wifiModule, Integer phase, int price, Double vatt, int capacity, String description) {
        this.setName(name);
        this.setBrand(brand);
        this.setWifiModule(wifiModule);
        this.setPhase(phase);
        this.setPrice(price);
        this.setVatt(vatt);
        this.setCapacity(capacity);
        this.setDescription(description);
    }
}
