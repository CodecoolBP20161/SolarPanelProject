package com.codecool.models;

import com.codecool.models.enums.ItemTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@EntityScan
@Data
@Table(name = "solarpanels")
public class SolarPanel extends Item implements Serializable{

    @NotEmpty
    private String optimalizer;

    @NotNull
    private Double watt;

    @NotNull
    private Integer capacity;

    private int priority = 3;

    public SolarPanel(){
        super();
        this.setPriority(3);
    }

    public SolarPanel(String name, String optimizer, Integer price, Double vat, Integer capacity, String description) {
        super(name, description, BigDecimal.valueOf(price), ItemTypeEnum.Item);
        this.setOptimalizer(optimizer);
        this.setWatt(vat);
        this.setCapacity(capacity);
        this.setPriority(3);
    }


}
