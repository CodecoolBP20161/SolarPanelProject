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
    private String brand;

    @NotEmpty
    private String name;

    @NotNull
    private int price;

    @NotEmpty
    private Integer phase;

    @NotEmpty
    private Double vatt;

    private String description;

    private Integer wifiModule;

    @NotNull
    private int capacity;


    public Inverter(String brand, String name, int price, Integer phase, Double vatt, String description, Integer wifiModule, int capacity) {
        this.setBrand(brand);
        this.setName(name);
        this.setPrice(price);
        this.setPhase(phase);
        this.setVatt(vatt);
        this.setDescription(description);
        this.setWifiModule(wifiModule);
        this.setCapacity(capacity);
    }

    public Inverter() {
    }
}
