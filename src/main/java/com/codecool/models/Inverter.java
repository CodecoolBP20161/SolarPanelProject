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
    private String optimizerName;

    @NotNull
    private int optimizerPrice;

    @NotNull
    private Integer phase;

    @NotNull
    private Double watt;

    private Integer wifiModule;

    @NotNull
    private int capacity;

    private int priority;


    public Inverter(InverterBrandEnum brand, String name, Integer price, Integer phase, Double watt, String description,
                    Integer wifiModule, Integer capacity, String optimizerName, int optimizerPrice) {
        super(name, description, BigDecimal.valueOf(price), ItemTypeEnum.Item);
        this.setBrand(brand);
        this.setPhase(phase);
        this.setWatt(watt);
        this.setWifiModule(wifiModule);
        this.setCapacity(capacity);
        this.setOptimizerName(optimizerName);
        this.setOptimizerPrice(optimizerPrice);
        this.priority = 1;
    }

    public Inverter() {
        super();
        priority = 1;
    }
}
