package com.codecool.models;

import com.codecool.models.enums.ItemTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@EntityScan
@Data
public class Inverter extends Item {

    @NotEmpty
    private String brand;

    @NotNull
    private String optimalizerName;

    @NotNull
    private int optimalierPrice;

    @NotNull
    private Integer phase;

    @NotNull
    private Double watt;

    private Integer wifiModule;

    @NotNull
    private int capacity;


    public Inverter(String brand, String name, Integer price, Integer phase, Double vatt, String description,
                    Integer wifiModule, Integer capacity, String optimalizerName, int optimalierPrice) {
        super(name, description, BigDecimal.valueOf(price), ItemTypeEnum.Item);
        this.setBrand(brand);
        this.setPhase(phase);
        this.setWatt(vatt);
        this.setWifiModule(wifiModule);
        this.setCapacity(capacity);
        this.setOptimalizerName(optimalizerName);
        this.setOptimalierPrice(optimalierPrice);
    }

    public Inverter() {
    }
}
