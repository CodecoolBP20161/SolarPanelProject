package com.codecool.models;

import com.codecool.models.enums.ItemTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@EntityScan
@Data
@Table(name = "solarpanel")
public class SolarPanel extends Item{

    @NotEmpty
    private String optimalizer;

    @NotNull
    private Double watt;

    @NotNull
    private Integer capacity;

    private int priority;

    public SolarPanel(){}

    public SolarPanel(String name, String optimizer, Integer price, Double vat, Integer capacity, String description) {
        super(name, description, BigDecimal.valueOf(price), ItemTypeEnum.Item);
        this.setOptimalizer(optimizer);
        this.setWatt(vat);
        this.setCapacity(capacity);
        this.priority = 0;
    }
}
