package com.codecool.models;

import com.codecool.models.enums.InverterBrandEnum;
import com.codecool.models.enums.ItemTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@EntityScan
@Data
public class Inverter extends Item {

    @NotNull
    private InverterBrandEnum brand;

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


    public Inverter(InverterBrandEnum brand, String name, Integer price, Integer phase, Double watt, String description,
                    Integer wifiModule, Integer capacity, String optimalizerName, int optimalierPrice) {
        super(name, description, BigDecimal.valueOf(price), ItemTypeEnum.Item);
        this.setBrand(brand);
        this.setPhase(phase);
        this.setWatt(watt);
        this.setWifiModule(wifiModule);
        this.setCapacity(capacity);
        this.setOptimalizerName(optimalizerName);
        this.setOptimalierPrice(optimalierPrice);
    }

    public Inverter() {
    }
}
