package com.codecool.models;

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
    private Double vat;

    @NotNull
    private Integer capacity;

    public SolarPanel(){}

    public SolarPanel(String name, String optimalizer, Integer price, Double vat, Integer capacity, String description) {
        super(name, description, BigDecimal.valueOf(price));
        this.setOptimalizer(optimalizer);
        this.setVat(vat);
        this.setCapacity(capacity);
    }
}
