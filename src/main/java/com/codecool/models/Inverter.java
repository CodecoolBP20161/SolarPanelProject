package com.codecool.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@EntityScan
@Data
public class Inverter extends Item {

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
        super(name, description, BigDecimal.valueOf(price));
        this.setBrand(brand);
        this.setPhase(phase);
        this.setVatt(vatt);
        this.setWifiModule(wifiModule);
        this.setCapacity(capacity);
    }

    public Inverter() {
    }
}
