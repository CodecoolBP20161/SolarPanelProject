package com.codecool.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Data
public class Inverter extends Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    private String brand;


    @NotNull
    private Integer phase;

    @NotNull
    private Double vatt;

    private Integer wifiModule;

    @NotNull
    private int capacity;


    public Inverter(String brand, String name, Integer price, Integer phase, Double vatt, String description, Integer wifiModule, Integer capacity) {
        this.setBrand(brand);
        this.setName(name);
        this.setPrice(BigDecimal.valueOf(price));
        this.setPhase(phase);
        this.setVatt(vatt);
        this.setDescription(description);
        this.setWifiModule(wifiModule);
        this.setCapacity(capacity);
    }

    public Inverter() {
    }
}
